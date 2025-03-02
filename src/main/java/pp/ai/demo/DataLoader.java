package pp.ai.demo;

import jakarta.annotation.PostConstruct;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Component;

@Component
public class DataLoader {

    private final VectorStore vectorStore;

    private final JdbcClient jdbcClient;

    @Value("classpath:/documents/pdf/*.pdf")
    private Resource[] pdfResources;

    public DataLoader(VectorStore vectorStore, JdbcClient jdbcClient) {
        this.vectorStore = vectorStore;
        this.jdbcClient = jdbcClient;
    }

    @PostConstruct
    public void init() {
        Integer count =
                jdbcClient.sql("select COUNT(*) from vector_store")
                        .query(Integer.class)
                        .single();

        System.out.println("No of Records in the PG Vector Store = " + count);

        if (count == 0) {
            System.out.println("Loading PDFs in the PG Vector Store");

            PdfDocumentReaderConfig config = PdfDocumentReaderConfig.builder()
                    .withPagesPerDocument(1)
                    .build();

            TokenTextSplitter textSplitter = new TokenTextSplitter();

            // Processar cada PDF na pasta
            for (Resource pdfResource : pdfResources) {
                try {
                    System.out.println("Processing: " + pdfResource.getFilename());

                    PagePdfDocumentReader reader = new PagePdfDocumentReader(pdfResource, config);
                    vectorStore.accept(textSplitter.apply(reader.get()));

                } catch (Exception e) {
                    System.err.println("Error processing file: " + pdfResource.getFilename());
                    e.printStackTrace();
                }
            }

            System.out.println("Application is ready to Serve the Requests");
        }
    }
}
