import re
import random

rules = {'I want (.*)': ['What would it mean if you got {0}',
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
             'name_greet': ['Hello, {}!', 'Hi there'],
             'thankyou': ['you are very welcome']}

# Define a dictionary of patterns
patterns = {}


# initialize data
def initialize():
    # Iterate over the keywords dictionary
    for intent, keys in keywords.items():
        # Create regular expressions and compile them into pattern objects
        patterns[intent] = re.compile('|'.join(keys))


# Define match_rule()
def match_rule(i_rules, message):
    message = message.lower()
    response, phrase = "um...", None

    # Iterate over the rules dictionary
    for pattern, responses in i_rules.items():
        # Create a match object
        match = re.search(pattern, message)
        if match is not None:
            # Choose a random response
            response = random.choice(responses)
            if '{0}' in response:
                phrase = match.group(1)
    # Return the response and phrase
    return response, phrase


# Define replace_pronouns()
def replace_pronouns(message):
    message = message.lower()
    if 'me' in message:
        # Replace 'me' with 'you'
        return re.sub('me', 'you', message)
    if 'my' in message:
        # Replace 'my' with 'your'
        return re.sub('my', 'your', message)
    if 'your' in message:
        # Replace 'your' with 'my'
        return re.sub('your', 'my', message)
    if 'you' in message:
        # Replace 'you' with 'me'
        return re.sub('you', 'I', message)
    if 'i' in message:
        # Replace 'I' with 'you'
        return re.sub(r'\bi\b', 'you', message)

    return message


def match_intent(message):
    matched_intent = None
    for intent, pattern in patterns.items():
        # Check if the pattern occurs in the message
        if pattern.search(message) is not None:
            matched_intent = intent
    return matched_intent


# Define find_name()
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


# Define greet()
def greet(message):
    # Find the name
    name = find_name(message)
    if name is None:
        return random.choice(responses['noname_greet'])
    else:
        return random.choice(responses['name_greet']).format(name)


# Define a respond function
def respond(message):
    # store a copy of the message in all lowercase
    message_nocaps = message.lower()
    # initialize the response variable
    response = None

    # Call the match_intent function
    intent = match_intent(message_nocaps)
    if not intent:
        # Call match_rule
        response, phrase = match_rule(rules, message_nocaps)
        if phrase is not None:
            if phrase.endswith('?'):
                phrase = phrase[:-1]
        if '{0}' in response:
            # Replace the pronouns in the phrase
            phrase = replace_pronouns(phrase)
            # Include the phrase in the response
            response = response.format(phrase)

    else:
        key = intent
        if key == 'greet':
            response = greet(message)
        else:
            response = random.choice(responses[key])

    return response


def send_message(message):
    return respond(message)


initialize()
