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
        this.customerProductDatabase = new CustomerProductDatabase(FileNames.CUSTOMERPRODUCT_FILENAME);
        productsDatabase.readFromFile();
        customerProductDatabase.readFromFile();
    }

    public boolean addProduct(String productID, String productName, String manufacturerName, String supplierName, int quantity, float price) {
    
        if(!productsDatabase.contains(productID))
        {
            Product product = new Product(productID, productName, manufacturerName, supplierName, quantity, price);
         productsDatabase.insertRecord(product);
        return true;
        }
        return false;
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

            CustomerProduct purchasingOperation = new CustomerProduct(customerSSN, productID, purchaseDate);
            customerProductDatabase.insertRecord(purchasingOperation);
            return true;
        }
        
        return false;
    }

    public int returnProduct(String customerSSN, String productID, LocalDate returnDate,LocalDate purchaseDate) {
        if (returnDate.isBefore(purchaseDate)) {
            return -1;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = purchaseDate.format(formatter);
        String key = customerSSN + "," + productID + "," + formattedDate;
        if (!customerProductDatabase.contains(key)) {
           return -2;
        }

        CustomerProduct purchasingOperation = customerProductDatabase.getRecord(key);
        LocalDate eligibleReturnDate = purchasingOperation.getPurchaseDate().plusDays(14);
        if (returnDate.isAfter(eligibleReturnDate)) {
            return -3;

        }

        Product product = productsDatabase.getRecord(productID);
        if (product == null) {
            return -4;
        }

        product.setQuantity(product.getQuantity() + 1);

        customerProductDatabase.deleteRecord(key);
        return 1;
    }

    public void logout() {
        productsDatabase.saveToFile();
        customerProductDatabase.saveToFile();
    }
}
