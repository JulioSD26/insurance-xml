import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

public class AvarageSales extends DefaultHandler {

    private static final String CLASS_NAME = AvarageSales.class.getName();
    private final static Logger LOG = Logger.getLogger(CLASS_NAME);

    private SAXParser parser = null;
    private SAXParserFactory spf;

    private double totalSales;
    private boolean inSales;
    private boolean inInsurance;
    private boolean inModel;

    private HashMap<String, Counter> subtotales;
    private String currentElement;

    private String insuranceValue;
    private String modelValue;


    // CONSTRUCTOR
    public AvarageSales(){
        super();
        spf = SAXParserFactory.newInstance();
        subtotales = new HashMap<>();
    }

    private void process(File file) {
        try {
            // obtener un parser para verificar el documento
            parser = spf.newSAXParser();

        } catch (SAXException | ParserConfigurationException e) {
            LOG.severe(e.getMessage());
            System.exit(1);
        }
        System.out.println("\nStarting parsing of " + file + "\n");
        try {
            // iniciar analisis del documento

            parser.parse(file, this);
        } catch (IOException | SAXException e) {
            LOG.severe(e.getMessage());
        }
    }


    @Override
    public void startDocument() throws SAXException {
        // al inicio del documento inicializar
        // las ventas totales
        totalSales = 0.0;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes)
            throws SAXException {

        if (qName.equals("insurance_record")) {
            inSales = true;
        }
        if (qName.equals("model")) {
            inModel = true;
        }
        if (qName.equals("insurance")) {
            inInsurance = true;
        }

        currentElement = qName;
    }

    // Obtener valores
    @Override
    public void characters(char[] bytes, int start, int length) throws SAXException {

        switch (currentElement) {
            case "model":
                this.modelValue = new String(bytes, start, length);
                break;
            case "insurance":
                this.insuranceValue = new String(bytes, start, length);
                break;

        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if ( qName.equals("insurance_record") ) {
            double val = 0.0;
            try {
                val = Double.parseDouble(this.insuranceValue);
            } catch (NumberFormatException e) {
                LOG.severe(e.getMessage());
            }

            if ( subtotales.containsKey(this.modelValue) ) {
                Counter c = subtotales.get(this.modelValue);
                c.plusOne();
                c.inc(val);

                subtotales.put( this.modelValue, c);
            } else {
                // crear el objeto counter
                Counter counter = new Counter(1, val);
                subtotales.put(this.modelValue, counter );
            }

            inSales = false;
        }
    }

    @Override
    public void endDocument() throws SAXException {
        // Se proceso todo el documento, imprimir resultado
        Set<Map.Entry<String,Counter>> entries = subtotales.entrySet();
        for (Map.Entry<String,Counter> entry: entries) {
            System.out.printf("%-15.15s $%,9.2f\n", entry.getKey(), entry.getValue().avarage());
        }
        System.out.printf("Ventas totales: $%,9.2f\n", totalSales);
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            LOG.severe("No file to process. Usage is:" + "\njava DeptSalesReport <keyword>");
            return;
        }
        File xmlFile = new File(args[0] );
        AvarageSales handler = new AvarageSales();
        handler.process( xmlFile );

    }


}
