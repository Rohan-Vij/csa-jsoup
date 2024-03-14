import pandas as pd
import random

def process_and_send(file_path, category):
    ads_file_path = f'./ads/{category}.csv'
    messages = pd.read_csv(ads_file_path)['ad_text'].tolist()
    
    df = pd.read_csv(file_path)
    for index, row in df.iterrows():
        user_id = row['Author']
        if "HP Team" in user_id:
            continue
        message = random.choice(messages)
        print(f"Sending to {user_id}: {message}")

file_paths = {
    'setup': './bad_setup_users.csv',
    'quality': './bad_quality_users.csv',
    'instruct': './bad_instruct_users.csv'
}

for category, file_path in file_paths.items():
    process_and_send(file_path, category)
