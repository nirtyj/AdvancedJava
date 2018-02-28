package demos.cassandra;

import demos.cassandra.domain.Book;
import demos.cassandra.repository.BookRepository;
import demos.cassandra.repository.KeyspaceRepository;

import java.util.logging.Logger;

import org.apache.log4j.BasicConfigurator;

import com.datastax.driver.core.Session;
import com.datastax.driver.core.utils.UUIDs;

public class CassandraClient {
    private static final Logger LOG = Logger.getLogger(CassandraClient.class.toString());

    public static void main(String args[]) {
    		BasicConfigurator.configure();
        CassandraConnector connector = new CassandraConnector();
        connector.connect("127.0.0.1", 9042);
        Session session = connector.getSession();

        KeyspaceRepository sr = new KeyspaceRepository(session);
        sr.createKeyspace("library", "SimpleStrategy", 1);
        sr.useKeyspace("library");

        BookRepository br = new BookRepository(session);
        br.createTable();
        br.alterTablebooks("publisher", "text");

        br.createTableBooksByTitle();

        Book book = new Book(UUIDs.timeBased(), "Effective Java", "Joshua Bloch", "Programming");
        Book book2 = new Book(UUIDs.timeBased(), "Nirty demo", "Joshua Bloch", "Programming");
        br.insertBookBatch(book);
        br.insertBookBatch(book2);
        
        br.selectAll().forEach(o -> LOG.info("Title in books: " + o.getTitle()));
        br.selectAllBookByTitle().forEach(o -> LOG.info("Title in booksByTitle: " + o.getTitle()));

        br.deletebookByTitle("Effective Java");
        br.deleteTable("books");
        br.deleteTable("booksByTitle");

        sr.deleteKeyspace("library");

        connector.close();
    }
}