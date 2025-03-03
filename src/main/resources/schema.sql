-- reset - WARNING: This will delete all data!
DROP TABLE IF EXISTS vector_store CASCADE;
DROP EXTENSION IF EXISTS vector CASCADE;
DROP EXTENSION IF EXISTS hstore CASCADE;
DROP EXTENSION IF EXISTS "uuid-ossp" CASCADE;

-- Recreate
CREATE EXTENSION IF NOT EXISTS vector;
CREATE EXTENSION IF NOT EXISTS hstore;
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE TABLE vector_store (
                              id UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
                              content TEXT NOT NULL,
                              metadata JSONB NOT NULL,
                              embedding vector(384) NOT NULL
);
CREATE INDEX vector_store_embedding_idx ON vector_store USING HNSW (embedding vector_cosine_ops);
