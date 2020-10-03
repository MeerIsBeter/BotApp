import re
import spacy

keywords = {'goodbye': ['bye', 'farewell'],
 'greet': ['hello', 'hi', 'hey'],
 'thankyou': ['thank', 'thx']}

responses = {'default': 'default message',
 'goodbye': 'goodbye for now',
 'greet': 'Hello you! :)',
 'thankyou': 'you are very welcome'}

# Define a dictionary of patterns
patterns = {}

# Iterate over the keywords dictionary
for intent, keys in keywords.items():
    # Create regular expressions and compile them into pattern objects
    patterns[intent] = re.compile('|'.join(keys))


# Define a function to find the intent of a message
def match_intent(message):
    matched_intent = None
    for intent, pattern in patterns.items():
        # Check if the pattern occurs in the message
        if pattern.search(message) is not None:
            matched_intent = intent
    return matched_intent


# Define find_name()
def find_name(message):
    name = None
    # Create a pattern for checking if the keywords occur
    name_keyword = re.compile('(name|call)')
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


# Define a respond function
def respond(message):
    # Call the match_intent function
    intent = match_intent(message)
    # Fall back to the default response
    key = "default"
    if intent in responses:
        key = intent
    return responses[key]


# Define respond()
def greet(message):
    # Find the name
    name = find_name(message)
    if name is None:
        return "Hi there!"
    else:
        return "Hello, {0}!".format(name)


# Load the spacy model: nlp
nlp = spacy.load('en_core_web_sm')

# Define included_entities
include_entities = ['DATE', 'ORG', 'PERSON']

# Define extract_entities()
def extract_entities(message):
    # Create a dict to hold the entities
    # ents = dict.fromkeys(include_entities)
    ents = {}
    # Create a spacy document
    doc = nlp(message)
    for ent in doc.ents:
        if ent.label_ not in ents.keys():
            # Save interesting entities
            ents[ent.label_] = [ent.text]
        else:
            ents[ent.label_].append(ent.text)
    return ents


def send_message(message):
    print(respond(message))


# Send messages
send_message("hello!")
send_message("bye byeee")
send_message("thanks very much!")

print(extract_entities("I'm looking for a Mexican restaurant in the North of town"))
print(extract_entities('people who graduated from MIT in 1999'))
