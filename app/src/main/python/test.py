import ELIZA_NL as NL
import ELIZA as EN
from EN_NL_CLASSIFIER import Classifier
import time


def respond_EN(message):
    # lang_ind_result, phrase = EN.match_rule(lang_ind, message)
    # if lang_ind_result != "um...":
    #     result = lang_ind_result
    # else:
    #     result = EN.send_message(message)
    
    result = EN.send_message(message)
    return result
        
        
def respond_NL(message):
    # lang_ind_result, phrase = NL.match_rule(lang_ind, message)
    # if lang_ind_result != "Aha":
    #     result = lang_ind_result
    # else:
    #     result = NL.send_message(message)
    
    result = NL.send_message(message)
    return result
        

def main(message):
    # cl = Classifier()
    # if cl.classify(message) == 0:
    #     result = respond_EN(message)
    # else:
    #     result = respond_NL(message)

    result = respond_EN(message)

    # USER_TEMPLATE = "USER: {}"
    # BOT_TEMPLATE = "BOT: {}"

    # print(USER_TEMPLATE.format(message))
    # print(BOT_TEMPLATE.format(result))
    # time.sleep(1.1)
    # print("")
    return result


