import pandas as pd

df1 = pd.read_csv('file1.csv')
df2 = pd.read_csv('file2.csv')

combined_df = pd.concat([df1, df2], ignore_index=True)

combined_df.to_csv('combined.csv', index=False)
