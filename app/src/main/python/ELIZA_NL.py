import re
import random

rules = {'ik wil (.*)': ['Wat zou het betekenen als je {0} zou krijgen',
                         'Waarom wil je {0}',
                         "Wat houd je tegen om {0} te krijgen"],
         'weet je nog dat (.*)': ['Denk je echt dat ik {0} zou vergeten',
                                  "Waarom ben je {0} niet vergeten?",
                                  '{0}? Wat is daarmee aan de hand?',
                                  'Yes .. and?'],
         'denk je dat (.*)': ['Of {0}? Natuurlijk.', 'Echt niet'],
         'snap je hoe (.*)': ['Hoe {0}? Ja, natuurlijk.', 'Nah'],
         'als (.*)': ["Denk je echt dat het waarschijnlijk is dat {0}",
                      'Zou je graag willen dat {0}',
                      'Wat vind je van {0}',
                      'Echt--al {0}'],
         'hoe gaat het': ['Prima hoor', 'redelijk', 'Heb me wel eens beter gevoeld moet ik zeggen'],
         'gaat het goed': ['Jep, prima', 'Ik heb geen emoties en/of lichaam, dus ik heb hier niet veel op te zeggen',
                           'mwoah']}


# Define match_rule()
def match_rule(i_rules, message):
    message = message.lower()
    response, phrase = "Aha", None

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
    if 'mij' in message:
        # Replace 'me' with 'you'
        return re.sub('mij', 'jou', message)
    if 'mijn' in message:
        # Replace 'my' with 'your'
        return re.sub('mijn', 'youw', message)
    if 'youw' in message:
        # Replace 'your' with 'my'
        return re.sub('youw', 'mijn', message)
    if 'jij' in message:
        # Replace 'you' with 'me'
        return re.sub('jij', 'ik', message)
    if 'ik' in message:
        # Replace 'I' with 'you'
        return re.sub(r'ik', 'jij', message)

    return message


# Define respond()
def respond(message):
    # Call match_rule
    response, phrase = match_rule(rules, message)
    if '{0}' in response:
        # Replace the pronouns in the phrase
        phrase = replace_pronouns(phrase)
        # Include the phrase in the response
        response = response.format(phrase)
    return response


def send_message(message):
    return respond(message)
