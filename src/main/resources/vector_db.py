import pandas as pd
import re
import chromadb

def get_tag(comment, keywords, rating):
    rating_scale = {5: 1.0, 4: 0.8, 3: 0.2, 2: -0.5, 1: -1.0}
    for keyword in keywords:
        if re.search(keyword, comment, re.IGNORECASE):
            return rating_scale.get(rating, 0)
    return 0

df = pd.read_csv('./912688787.csv')

keywords_set_up = ['set up', 'setup']
keywords_quality = ['quality']
keywords_instruct = ['instruct']

df['setup_score'] = df.apply(lambda row: get_tag(row['Comment'], keywords_set_up, row['Rating']), axis=1)
df['quality_score'] = df.apply(lambda row: get_tag(row['Comment'], keywords_quality, row['Rating']), axis=1)
df['instruct_score'] = df.apply(lambda row: get_tag(row['Comment'], keywords_instruct, row['Rating']), axis=1)

chroma_client = chromadb.Client()
collection_name = "reviews"

collection = chroma_client.get_or_create_collection(name=collection_name)

embeddings = df[['setup_score', 'quality_score', 'instruct_score']].values.tolist()
documents = df['Comment'].tolist()
metadatas = df[['Rating', 'Author', 'Date', 'Verified?']].to_dict(orient='records')
ids = [f"id_{i}" for i in range(len(df))]

collection.add(
    embeddings=embeddings,
    documents=documents,
    metadatas=metadatas,
    ids=ids
)

traits = ['setup', 'quality', 'instruct']

for trait in traits:
    score_column = f"{trait}_score"
    negative_trait_df = df[df[score_column] < 0]
    
    file_path = f'user_lists/bad_{trait}_users.csv'
    negative_trait_df.to_csv(file_path, index=False)

    print(f"Saved {len(negative_trait_df)} negative {trait} reviews to '{file_path}'")

print(f"Added {len(df)} documents to collection {collection_name}")
print(collection.get(include=[]))
