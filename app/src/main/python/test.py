import ELIZA_NL as NL
import ELIZA as EN
# from EN_NL_CLASSIFIER import Classifier
import time

# clf = Classifier()

lang_ind = {'warudo': ["HACHIBYOU KEIKA! WRYYYYYYYY",
                       "*distant ora-ora noises*",
                       "SUTANDO POWAAAH!!!",
                       "Hinjaku, hinjaku!",
                       "Za Warudo ga saikyou no sutando da",
                       "MUDAMUDAMUDAMUDA",
                       "*bass-boosted Za Warudo noises*",
                       "Kono Dio da!"],
            'dio': ["HACHIBYOU KEIKA! WRYYYYYYYY",
                       "*distant ora-ora noises*",
                       "SUTANDO POWAAAH!!!",
                       "Hinjaku, hinjaku!",
                       "Za Warudo ga saikyou no sutando da",
                       "MUDAMUDAMUDAMUDA",
                       "*bass-boosted Za Warudo noises*",
                       "Kono Dio da!"],
            'jotaro': ["Yare yare daze", "YAKAMASHI!"],
            'kakyoin': ["Rerorerorerorero", "Cherries are my favorite food"],
            'iggy': ["Dead and buried", "Ded"]}


def main(message):
    lang_ind_result, phrase = EN.match_rule(lang_ind, message)
    if lang_ind_result != "um...":
        result = lang_ind_result
    # elif clf.classify(message) == 0:
    #    result = EN.send_message(message)
    else:
        result = EN.send_message(message)

    # USER_TEMPLATE = "USER: {}"
    BOT_TEMPLATE = "BOT: {}"

    # print(USER_TEMPLATE.format(message))
    # print(BOT_TEMPLATE.format(result))
    # time.sleep(1.1)
    # print("")
    return result


