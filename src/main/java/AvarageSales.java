import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class AvarageSales extends DefaultHandler {





    @Override
    public void startDocument() throws SAXException {
        // al inicio del documento inicializar
        // las ventas totales
        totalSales = 0.0;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        // Elemento actual que se esta procesando
        if( localName.equals("sales") )        {
            //  <sales>: entramos al elemento
            inSales = true;
        }
    }

    @Override
    public void endDocument() throws SAXException {
        // Se proceso todo el documento, imprimir resultado
        System.out.printf("Ventas totales: $%,8.2f\n", totalSales);
    }


}
