-- 🚨 WARNING: This will delete all data!
DROP TABLE IF EXISTS vector_store CASCADE;

-- Drop extensions if they exist (ensures a clean reset)
DROP EXTENSION IF EXISTS vector CASCADE;
DROP EXTENSION IF EXISTS hstore CASCADE;
DROP EXTENSION IF EXISTS "uuid-ossp" CASCADE;

-- Recreate necessary PostgreSQL extensions
CREATE EXTENSION IF NOT EXISTS vector;
CREATE EXTENSION IF NOT EXISTS hstore;
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Recreate the vector_store table
CREATE TABLE vector_store (
                              id UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
                              content TEXT NOT NULL,
                              metadata JSONB NOT NULL,
                              embedding vector(384) NOT NULL
);

-- Create index for vector similarity search
CREATE INDEX vector_store_embedding_idx ON vector_store USING HNSW (embedding vector_cosine_ops);

-- Optional: Log table reset
SELECT 'Database reset complete: vector_store table recreated' AS status;