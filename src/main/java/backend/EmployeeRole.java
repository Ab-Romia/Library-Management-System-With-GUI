package backend;

import constants.FileNames;

import backend.ProductDatabase;
import backend.CustomerProductDatabase;
import backend.CustomerProduct;
import backend.Product;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author mohab
 */
public class EmployeeRole {
    private ProductDatabase productsDatabase;
    private CustomerProductDatabase customerProductDatabase;

    public EmployeeRole() {
        this.productsDatabase = new ProductDatabase(FileNames.PRODUCT_FILENAME);
        this.customerProductDatabase = new CustomerProductDatabase(FileNames.PRODUCT_FILENAME);
        this.productsDatabase.readFromFile();
        this.customerProductDatabase.readFromFile();
    }

    public void addProduct(String productID, String productName, String manufacturerName, String supplierName, int quantity, float price) {
        Product product = new Product(productID, productName, manufacturerName, supplierName, quantity, price);
        productsDatabase.insertRecord(product);
        productsDatabase.saveToFile();
    }

    public Product[] getListOfProducts() {
        return productsDatabase.returnAllRecords().toArray(new Product[0]);
    }

    public CustomerProduct[] getListOfPurchasingOperations() {
        return customerProductDatabase.returnAllRecords().toArray(new CustomerProduct[0]);
    }

    public boolean purchaseProduct(String customerSSN, String productID, LocalDate purchaseDate) {
        Product product = productsDatabase.getRecord(productID);
        if (product != null && product.getQuantity() > 0) {
            product.setQuantity(product.getQuantity() - 1);
            productsDatabase.saveToFile();

            CustomerProduct purchasingOperation = new CustomerProduct(customerSSN, productID, purchaseDate);
            customerProductDatabase.insertRecord(purchasingOperation);
            customerProductDatabase.saveToFile();

            return true;
        }
        return false;
    }

    public double returnProduct(String customerSSN, String productID, LocalDate purchaseDate, LocalDate returnDate) {
        if (returnDate.isBefore(purchaseDate)) {
            return -1;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = purchaseDate.format(formatter);
        String key = customerSSN + "," + productID + "," + formattedDate;
        if (!customerProductDatabase.contains(key)) {
            return -1;
        }

        CustomerProduct purchasingOperation = customerProductDatabase.getRecord(key);
        LocalDate eligibleReturnDate = purchasingOperation.getPurchaseDate().plusDays(14);
        if (returnDate.isAfter(eligibleReturnDate)) {
            return -1;
        }

        Product product = productsDatabase.getRecord(productID);
        if (product == null) {
            return -1;
        }

        product.setQuantity(product.getQuantity() + 1);
        productsDatabase.saveToFile();

        customerProductDatabase.deleteRecord(key);
        customerProductDatabase.saveToFile();

        return product.getPrice();
    }

    public void logout() {
        productsDatabase.saveToFile();
        customerProductDatabase.saveToFile();
    }
}
