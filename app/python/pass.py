import hashlib
import random

def generate_password(site, master_password, pass_length):
    length = pass_length
    if length > 16:
        length = 16

    combined = site + master_password
    hash_object = hashlib.sha256(combined.encode())
    hex_dig = hash_object.hexdigest()
    random.seed(hex_dig)

    dictionary = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz0123456789"
    special_chars = ''.join(random.sample("1234567890!@#$%^&*()-_+=", length + 1))
    base_password_list = []

    for j in range(length):
        f = random.randint(1, len(dictionary))
        place_spec_char = random.randint(1, 4)
        if place_spec_char == 1:
            base_password_list.append(special_chars[j])
        else:
            base_password_list.append(dictionary[f - 1])

    password = ''.join(base_password_list)
    return password
