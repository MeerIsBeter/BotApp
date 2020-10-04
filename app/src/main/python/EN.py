import re
import random

rules = {'i want (.*)': ['What would it mean if you got {0}',
                         'Why do you want {0}',
                         "What's stopping you from getting {0}"],
         'do you remember (.*)': ['Did you think I would forget {0}',
                                  "Why haven't you been able to forget {0}",
                                  'What about {0}',
                                  'Yes .. and?'],
         'do you think (.*)': ['if {0}? Absolutely.', 'No chance'],
         'do you understand (.*)': ['{0}? Yes, of course.', 'No way'],
         'do you know (.*)': ['{0}? Yes, of course.', "No, I don't"],
         'if (.*)': ["Do you really think it's likely that {0}",
                     'Do you wish that {0}',
                     'What do you think about {0}',
                     'Really--if {0}'],
         'how are you': ["I'm alright, thanks", "Ok I guess", "I've felt better"]}

keywords = {'goodbye': ['bye', 'farewell'],
            'greet': ['hello', 'hi', 'hey'],
            'thankyou': ['thank', 'thx']}

responses = {'default': 'default message',
             'goodbye': ['goodbye for now'],
             'noname_greet': ['Hello you! :)', 'Hi there', 'Hello!'],
             'name_greet': ['Hello, {}!', 'Hi there {}'],
             'thankyou': ['you are very welcome', 'np', "don't worry about it"]}

# Define a dictionary of patterns
patterns = {}


# initialize data
def initialize():
    for intent, keys in keywords.items():
        # Create regular expressions and compile them into pattern objects
        regexp = r'{}'.format('|'.join(map(lambda x: r'\b{}\b'.format(x), keys)))
        patterns[intent] = re.compile(regexp)


# check the input for more complex rules
def match_rule(i_rules, message):
    message = message.lower()
    response, phrase = "um...", None

    for pattern, responses in i_rules.items():
        # Create a match object
        match = re.search(pattern, message)
        if match is not None:
            response = random.choice(responses)
            if '{0}' in response:
                phrase = match.group(1)
            break
    # Return the response and phrase
    return response, phrase


# replace the pronouns in the input
def replace_pronouns(message):
    words = message.lower().split()

    for i in range(len(words)):
        if words[i] == 'me':
            # Replace 'me' with 'you'
            words[i] = 'you'
        elif words[i] == 'my':
            # Replace 'my' with 'your'
            words[i] = 'your'
        elif words[i] == 'you':
            # Replace 'you' with 'me'
            words[i] = 'me'
        elif words[i] == 'your':
            # Replace 'your' with 'my'
            words[i] = 'my'
        elif words[i] == 'i':
            # Replace 'I' with 'you'
            words[i] = 'you'

    return ' '.join(words)


# check the input for a simple intent
def match_intent(message):
    matched_intent = None
    for intent, pattern in patterns.items():
        # Check if the pattern occurs in the message
        if pattern.search(message) is not None:
            matched_intent = intent
    return matched_intent


# determine the presence of a name in the input
def find_name(message):
    # exclude the first character from the input, as that character may be capitalized
    message = message[1:]
    name = None
    # Create a pattern for checking if the keywords occur
    name_keyword = re.compile('(name|call|I am)')
    # Create a pattern for finding capitalized words
    name_pattern = re.compile('[A-Z]{1}[a-z]*')
    if name_keyword.search(message):
        # Get the matching words in the string
        # name_phrase = name_keyword.search(message).group(1)
        name_words = name_pattern.findall(message)
        if len(name_words) > 0:
            # Return the name if the keywords are present
            name = ' '.join(name_words)
    return name


# construct a greeting
def greet(message):
    # Find the name
    name = find_name(message)
    if name is None:
        return random.choice(responses['noname_greet'])
    else:
        return random.choice(responses['name_greet']).format(name)


# construct the response
def respond(message):
    message_nocaps = message.lower()
    response = None

    # first scan the input for words that could signal a simple intent
    intent = match_intent(message_nocaps)

    if intent:
        if intent == 'greet':
            response = greet(message)
        else:
            response = random.choice(responses[intent])
    else:
        # scan the input for more complex patterns
        response, phrase = match_rule(rules, message_nocaps)
        # 'phrase' corresponds to a section of the input that could be repeated in the answer
        if phrase is not None:
            if phrase.endswith('?'):
                phrase = phrase[:-1]
        # if the selected response format contains a to-be-filled-in section, put the phrase there
        if '{0}' in response:
            phrase = replace_pronouns(phrase)
            response = response.format(phrase)

    return response


def send_message(message):
    return respond(message)


initialize()
