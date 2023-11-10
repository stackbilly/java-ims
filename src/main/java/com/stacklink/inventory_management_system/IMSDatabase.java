package com.stacklink.inventory_management_system;

import com.mongodb.MongoWriteException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Updates;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class IMSDatabase {
    static final String DB_NAME = "InventoryDB";

    DialogWindow dialog = new DialogWindow();

    public void createUniqueIndex(String fieldName, String collectionName) {
        Document index = new Document(fieldName, 1);
        MongoClient mongodbClient = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase database = mongodbClient.getDatabase(DB_NAME);
        MongoCollection<Document> collection = database.getCollection(collectionName);
        collection.createIndex(index, new IndexOptions().unique(true));
    }

    public boolean addCategory(String categoryName, String type) {
        try {
            MongoClient mongodbClient = MongoClients.create("mongodb://localhost:27017");
            MongoDatabase database = mongodbClient.getDatabase(DB_NAME);
            System.out.println("Connection Status: [OK], Database name: " + database.getName());

            String collectionName = "Category";

            MongoCollection<Document> collection = database.getCollection(collectionName);

            createUniqueIndex("name", collectionName);

            Document document = new Document("name", categoryName).append("type", type);
            try {
                collection.insertOne(document);
                System.out.println("Category doc insertion: [GOOD STATE]");
                return true;
            } catch (MongoWriteException e) {
                dialog.showDialog("Exception!", categoryName + " category already exists!");
            }

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return false;
    }

    public ObservableList<Category> getCategories() {
        String collectionName = "Category";
        ObservableList<Category> categories = FXCollections.observableArrayList();

        try {
            MongoClient mongodbClient = MongoClients.create("mongodb://localhost:27017");
            MongoDatabase database = mongodbClient.getDatabase(DB_NAME);
            System.out.println("Connection Status: [OK], Database name: " + database.getName());

            MongoCollection<Document> collection = database.getCollection(collectionName);
            FindIterable<Document> documents = collection.find();
            int i = 1;
            for (Document doc : documents) {
                categories.add(new Category(String.valueOf(i), (String) doc.get("name"), (String) doc.get("type")));
                i++;
            }
        } catch (Exception e) {
            dialog.showDialog("Exception", "Error occurred while retrieving data");
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return categories;
    }

    public ArrayList<String> getCategoryData(String type) {
        ArrayList<String> categories = new ArrayList<>();
        String collectionName = "Category";
        try {
            MongoClient mongodbClient = MongoClients.create("mongodb://localhost:27017");
            MongoDatabase database = mongodbClient.getDatabase(DB_NAME);
            System.out.println("Connection Status: [OK], Database name: " + database.getName());

            MongoCollection<Document> collection = database.getCollection(collectionName);
            FindIterable<Document> documents = collection.find(Filters.eq("type", type));
            for (Document doc : documents) {
                categories.add((String) doc.get("name"));
            }
        } catch (Exception e) {
            dialog.showDialog("Exception", "Error occurred while retrieving data");
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return categories;
    }

    public boolean addProduct(Product product) {
        boolean isInserted = false;
        try {
            MongoClient mongodbClient = MongoClients.create("mongodb://localhost:27017");
            MongoDatabase database = mongodbClient.getDatabase(DB_NAME);

            System.out.println("Database connection: [OK] " + database.getName() + "\nADDR: PRODUCT");
            String collectionName = "Product";

            MongoCollection<Document> collection = database.getCollection(collectionName);

            createUniqueIndex("code", collectionName);

            Document document = new Document("name", product.getProductName()).append("description", product.getDescription())
                    .append("code", product.getProductCode())
                    .append("category", product.getCategory()).append("quantity", Integer.parseInt(product.getQuantity()))
                    .append("cost", Integer.parseInt(product.getCostPrice())).append("total", Integer.parseInt(product.getQuantity()) *
                            Integer.parseInt(product.getCostPrice()))
                    .append("sale", Integer.parseInt(product.getSalePrice())).
                    append("expectedSales", Integer.parseInt(product.getQuantity()) * Integer.parseInt(product.getSalePrice()))
                    .append("date", new SimpleDateFormat("yyyy-MM-dd").parse(product.getDateAdded()));
            try {
                collection.insertOne(document);
                System.out.println("Document insertion: [GOOD STATE]!");
                isInserted = true;
            } catch (MongoWriteException e) {
                dialog.showDialog("Exception", "Document already exists, duplicates aren't allowed");
            }
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return isInserted;
    }

    public ObservableList<Product> getProducts() {
        String collectionName = "Product";
        ObservableList<Product> products = FXCollections.observableArrayList();
        try {
            MongoClient mongodbClient = MongoClients.create("mongodb://localhost:27017");
            MongoDatabase database = mongodbClient.getDatabase(DB_NAME);
            System.out.println("Database connection: [GOOD STATE], /route -> " + collectionName);

            MongoCollection<Document> collection = database.getCollection(collectionName);
            FindIterable<Document> documents = collection.find();
            int i = 1;
            getProduct(products, documents, i);
        } catch (Exception e) {
            dialog.showDialog("Exception", "Exception while extracting data from database!");
            System.out.printf("%s %s%n", e.getClass().getName(), e.getMessage());
        }
        return products;
    }

    private void getProduct(ObservableList<Product> products, FindIterable<Document> documents, int i) {
        for (Document doc : documents) {
            products.add(new Product(String.valueOf(i), (String) doc.get("name"), (String) doc.get("description"), (String) doc.get("code"),
                    (String) doc.get("category"), (Integer) doc.get("quantity"), (Integer) doc.get("cost"),
                    (Integer) doc.get("sale"), String.valueOf(doc.get("date")), (Integer)
                    doc.get("total"), (Integer) doc.get("expectedSales")));
            i++;
        }
    }

    public ObservableList<Product> getProducts(String code) {
        String collectionName = "Product";
        ObservableList<Product> products = FXCollections.observableArrayList();
        try {
            MongoClient mongodbClient = MongoClients.create("mongodb://localhost:27017");
            MongoDatabase database = mongodbClient.getDatabase(DB_NAME);
            System.out.println("Database connection: [GOOD STATE], /route -> " + collectionName);

            MongoCollection<Document> collection = database.getCollection(collectionName);
            FindIterable<Document> documents = collection.find(Filters.eq("code", code));
            int i = 1;
            getProduct(products, documents, i);
        } catch (Exception e) {
            dialog.showDialog("Exception", "Exception while extracting data from database!");
            System.out.printf("%s %s%n", e.getClass().getName(), e.getMessage());
        }
        return products;
    }

    public boolean updateProduct(String collectionName, String fieldName, String oldValue, String newValue) {
        boolean isUpdate = false;
        try {
            MongoClient mongodbClient = MongoClients.create("mongodb://localhost:27017");
            MongoDatabase database = mongodbClient.getDatabase(DB_NAME);

            MongoCollection<Document> collection = database.getCollection(collectionName);
            collection.updateOne(Filters.eq(fieldName, oldValue), Updates.set(fieldName, newValue));
            isUpdate = true;
            System.out.println(fieldName + " update state: ->/ [GOOD]");
        } catch (MongoWriteException e) {
            System.out.println(fieldName + " update state: ->/ [BAD]");
            System.out.printf("%s %s%n", e.getClass().getName(), e.getMessage());
        }
        return isUpdate;
    }

    public boolean updateProduct(String collectionName, String fieldName, int oldValue, int newValue) {
        boolean isUpdate = false;
        try {
            MongoDatabase database;
            try (MongoClient mongodbClient = MongoClients.create("mongodb://localhost:27017")) {
                database = mongodbClient.getDatabase(DB_NAME);
            }

            MongoCollection<Document> collection = database.getCollection(collectionName);
            collection.updateOne(Filters.eq(fieldName, oldValue), Updates.set(fieldName, newValue));
            isUpdate = true;
            System.out.println(fieldName + " update state: ->/ [GOOD]");
        } catch (MongoWriteException e) {
            System.out.println(fieldName + " update state: ->/ [BAD]");
            System.out.printf("%s %s%n", e.getClass().getName(), e.getMessage());
        }
        return isUpdate;
    }

    public boolean updateProduct(String productName, int quantity) {
        boolean onUpdate;
        try {
            MongoClient mongodbClient = MongoClients.create("mongodb://localhost:27017");
            MongoDatabase database = mongodbClient.getDatabase(DB_NAME);

            MongoCollection<Document> collection = database.getCollection("Product");
            FindIterable<Document> documents = collection.find(Filters.eq("name", productName));
            int mQuantity = 0;
            for (Document doc : documents)
                mQuantity = Integer.parseInt(String.valueOf(doc.get("quantity")));
            collection.updateOne(Filters.eq("name", productName), Updates.set("quantity", mQuantity + quantity));
            System.out.println(productName + " update state: ->/ [GOOD]");
            onUpdate = true;
        } catch (MongoWriteException e) {
            System.out.println(productName + " update state: ->/ [BAD]");
            onUpdate = false;
            System.out.printf("%s %s%n", e.getClass().getName(), e.getMessage());
        }
        return onUpdate;
    }

    public ArrayList<String> getSaleData(String code) {
        ArrayList<String> saleData = new ArrayList<>();
        try {
            MongoClient mongodbClient = MongoClients.create("mongodb://localhost:27017");
            MongoDatabase database = mongodbClient.getDatabase(DB_NAME);

            MongoCollection<Document> collection = database.getCollection("Product");
            FindIterable<Document> documents = collection.find(Filters.eq("code", code));
            for (Document doc : documents) {
                saleData.add((String) doc.get("name"));
                saleData.add(String.valueOf(doc.get("quantity")));
                saleData.add(String.valueOf(doc.get("sale")));
                saleData.add(String.valueOf(doc.get("cost")));
            }
        } catch (Exception e) {
            System.out.printf("%s %s%n", e.getClass().getName(), e.getMessage());
        }
        return saleData;
    }

    public boolean addSaleData(Cart cart, String getCost) {
        boolean isInserted = false;
        try {
            MongoClient mongodbClient = MongoClients.create("mongodb://localhost:27017");
            MongoDatabase database = mongodbClient.getDatabase(DB_NAME);

            MongoCollection<Document> collection = database.getCollection("Sales");

            createUniqueIndex("saleID", "Sales");
            int mTotal = Integer.parseInt(cart.getQuantity()) * Integer.parseInt(cart.getPrice());
            int pTotal = Integer.parseInt(cart.getQuantity()) * Integer.parseInt(getCost);
            int profit = mTotal - pTotal;
            Document document = new Document("saleID", ThreadLocalRandom.current().nextInt(11111, 99999))
                    .append("code", cart.getCode()).append("name", cart.getName()).append("quantity", Integer.parseInt(cart.getQuantity())).append("price", Integer.parseInt(cart.getPrice()))
                    .append("total", mTotal)
                    .append("profit", profit).append("Date", LocalDate.now().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))).append("status", "notSold");
            try {
                collection.insertOne(document);
                System.out.println("Cart inserted [SUCCESS] ->/ route = sales");
                System.out.println(LocalDate.now());
                isInserted = true;
            } catch (MongoWriteException e) {
                addSaleData(cart, getCost);
                System.out.printf("%s %s%n", e.getClass().getName(), e.getMessage());
            }
        } catch (Exception e) {
            System.out.printf("%s %s%n", e.getClass().getName(), e.getMessage());
        }
        return isInserted;
    }

    public ObservableList<Cart> getSaleData() {
        ObservableList<Cart> saleData = FXCollections.observableArrayList();
        try {
            MongoClient mongodbClient = MongoClients.create("mongodb://localhost:27017");
            MongoDatabase database = mongodbClient.getDatabase(DB_NAME);

            MongoCollection<Document> collection = database.getCollection("Sales");

            FindIterable<Document> documents = collection.find(Filters.eq("status", "notSold"));
            for (Document doc : documents)
                saleData.add(new Cart(String.valueOf(doc.get("saleID")), (String) doc.get("code"), (String) doc.get("name"),
                        String.valueOf(doc.get("quantity")), String.valueOf(doc.get("price")), String.valueOf(doc.get("total")),
                        String.valueOf(doc.get("date"))));
        } catch (Exception e) {
            System.out.printf("%s %s%n", e.getClass().getName(), e.getMessage());
        }
        return saleData;
    }

    public void deleteCartItem(int saleID) {
        try {
            MongoClient mongodbClient = MongoClients.create("mongodb://localhost:27017");
            MongoDatabase database = mongodbClient.getDatabase(DB_NAME);

            MongoCollection<Document> collection = database.getCollection("Sales");
            collection.deleteOne(Filters.eq("saleID", saleID));
        } catch (Exception e) {
            System.out.printf("%s %s%n", e.getClass().getName(), e.getMessage());
        }
    }

    public void addPaySlip(PaySlip paySlip) {
        try {
            MongoClient mongodbClient = MongoClients.create("mongodb://localhost:27017");
            MongoDatabase database = mongodbClient.getDatabase(DB_NAME);

            createUniqueIndex("slipID", "Slips");
            MongoCollection<Document> collection = database.getCollection("Slips");
            Document document = new Document("slipID", ThreadLocalRandom.current().nextInt(11111, 99999)).append("total", paySlip.getSubTotal())
                    .append("pay", paySlip.getAmountPaid()).append("balance", paySlip.getBalance())
                    .append("date", LocalDateTime.now());
            try {
                collection.insertOne(document);
                System.out.println("INSERTION ->/ payslip: [SUCCESS]");
            } catch (MongoWriteException e) {
                dialog.showDialog("Exception", "Slip with that ID already exists");
            }
        } catch (Exception e) {
            System.out.printf("%s %s%n", e.getClass().getName(), e.getMessage());
        }
    }

    public int getProductQuantity(String productName) {
        int quantity = 0;
        try {
            MongoClient mongodbClient = MongoClients.create("mongodb://localhost:27017");
            MongoDatabase database = mongodbClient.getDatabase(DB_NAME);
            System.out.println("Database CONN -> [SUCCESS] -route -> Product");

            MongoCollection<Document> collection = database.getCollection("Product");
            FindIterable<Document> documents = collection.find(Filters.eq("name", productName));
            for (Document doc : documents)
                quantity = Integer.parseInt(String.valueOf(doc.get("quantity")));

        } catch (Exception e) {
            dialog.showDialog("Exception", "Error completing transaction");
            System.out.printf("%s %s%n", e.getClass().getName(), e.getMessage());
        }
        return quantity;
    }

    public void updateProductOnSale(String code, int quantity) {
        int newQuantity;
        try {
            MongoClient mongodbClient = MongoClients.create("mongodb://localhost:27017");
            MongoDatabase database = mongodbClient.getDatabase(DB_NAME);
            System.out.println("Database CONN -> [SUCCESS] -route -> Product");

            MongoCollection<Document> collection = database.getCollection("Product");
            FindIterable<Document> documents = collection.find(Filters.eq("code", code));
            int _OrgQuantity = 0;
            for (Document doc : documents)
                _OrgQuantity = Integer.parseInt(String.valueOf(doc.get("quantity")));
            newQuantity = _OrgQuantity - quantity;
            collection.updateOne(Filters.eq("code", code), Updates.set("quantity", newQuantity));
        } catch (Exception e) {
            dialog.showDialog("Exception", "Error completing transaction");
            System.out.printf("%s %s%n", e.getClass().getName(), e.getMessage());
        }
    }



    public void updateSellState() {
        try {
            MongoClient mongodbClient = MongoClients.create("mongodb://localhost:27017");
            MongoDatabase database = mongodbClient.getDatabase(DB_NAME);
            System.out.println("Database CONN -> [SUCCESS] -route -> Sales");

            MongoCollection<Document> collection = database.getCollection("Sales");

            collection.updateMany(Filters.eq("status",
                    "notSold"), Updates.set("status", "Sold"));

        } catch (Exception e) {
            dialog.showDialog("Exception", "Error completing transaction");
            System.out.printf("%s %s%n", e.getClass().getName(), e.getMessage());
        }
    }

    public int getCollectionCount(String collectionName) {
        int noOfProducts = 0;
        try {
            MongoClient mongodbClient = MongoClients.create("mongodb://localhost:27017");
            MongoDatabase database = mongodbClient.getDatabase(DB_NAME);
            System.out.println("Database CONN -> [SUCCESS] -route -> Products");
            MongoCollection<Document> collection = database.getCollection(collectionName);
            noOfProducts = (int) collection.countDocuments();
        } catch (Exception e) {
            dialog.showDialog("Exception", "Error reading the database");
            System.out.printf("%s %s%n", e.getClass().getName(), e.getMessage());
        }
        return noOfProducts;
    }

    public int getTotalSales(String date) {
        int total = 0;
        try {
            MongoClient mongodbClient = MongoClients.create("mongodb://localhost:27017");
            MongoDatabase database = mongodbClient.getDatabase(DB_NAME);

            MongoCollection<Document> collection = database.getCollection("Sales");
            Bson statusFilter = Filters.eq("status", "Sold");
            if (date.isEmpty()){
                FindIterable<Document> documents = collection.find(Filters.eq("status", "Sold"));
                for (Document doc : documents)
                    total += Integer.parseInt(String.valueOf(doc.get("profit")));
            }
            if (!date.isEmpty()){
                Bson dateFilter = Filters.eq("Date", formatDate(date));
                FindIterable<Document> documents = collection.find(Filters.and(statusFilter, dateFilter));
                for (Document doc : documents)
                    total += Integer.parseInt(String.valueOf(doc.get("profit")));
            }
        } catch (Exception e) {
            dialog.showDialog("Exception", "Exception occurred while reading data");
            System.out.printf("%s %s%n", e.getClass().getName(), e.getMessage());
        }
        return total;
    }

    public ObservableList<Sales> getAllSales() {
        ObservableList<Sales> listOfSales = FXCollections.observableArrayList();
        try {
            MongoClient mongodbClient = MongoClients.create("mongodb://localhost:27017");
            MongoDatabase database = mongodbClient.getDatabase(DB_NAME);
            MongoCollection<Document> collection = database.getCollection("Sales");
            FindIterable<Document> documents = collection.find(Filters.eq("status", "Sold"));
            int i;
            i = 1;
            populateSales(listOfSales, documents, i);
        } catch (Exception e) {
            dialog.showDialog("Exception", "Exception while retrieving sales");
            System.out.printf("%s %s%n", e.getClass().getName(), e.getMessage());
        }
        return listOfSales;
    }

    private void populateSales(ObservableList<Sales> listOfSales, FindIterable<Document> documents, int i) {
        for (Document doc : documents) {
            listOfSales.add(new Sales(
                    String.valueOf(i),
                    (String) doc.get("Date"),
                    String.valueOf(doc.get("saleID")),
                    (String) doc.get("code"),
                    (String) doc.get("name"),
                    String.valueOf(doc.get("quantity")),
                    String.valueOf(doc.get("price")),
                    String.valueOf(doc.get("profit"))
            ));
            ++i;
        }
    }

    public String formatDate(String date) {
        LocalDate _Date = LocalDate.parse(date);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d, yyyy");
        System.out.println(_Date.format(formatter));
        return _Date.format(formatter);
    }

    public ObservableList<Sales> getSalesByDate(String date) {
        ObservableList<Sales> listOfSales = FXCollections.observableArrayList();
        try {
            MongoClient mongodbClient = MongoClients.create("mongodb://localhost:27017");
            MongoDatabase database = mongodbClient.getDatabase(DB_NAME);
            MongoCollection<Document> collection = database.getCollection("Sales");
            Bson statusFilter = Filters.eq("status", "Sold");
            Bson dateFilter = Filters.eq("Date", formatDate(date));
            FindIterable<Document> documents = collection.find(Filters.and(statusFilter, dateFilter));
            int i = 1;
            populateSales(listOfSales, documents, i);
        } catch (Exception e) {
            dialog.showDialog("Exception", "Exception while retrieving sales");
            System.out.printf("%s %s%n", e.getClass().getName(), e.getMessage());
        }
        return listOfSales;
    }

    public ObservableList<Sales> getSalesBySaleID(String saleID) {
        ObservableList<Sales> listOfSales = FXCollections.observableArrayList();
        try {
            MongoClient mongodbClient = MongoClients.create("mongodb://localhost:27017");
            MongoDatabase database = mongodbClient.getDatabase(DB_NAME);
            MongoCollection<Document> collection = database.getCollection("Sales");
            Bson statusFilter = Filters.eq("status", "Sold");
            Bson saleIDFilter = Filters.eq("saleID", Integer.parseInt(saleID));
            FindIterable<Document> documents = collection.find(Filters.and(statusFilter, saleIDFilter));
            if (saleID.length() > 1) {
                int i = 1;
                populateSales(listOfSales, documents, i);
            }
            if (saleID.isEmpty()) {
                listOfSales = getAllSales();
            }
        } catch (Exception e) {
            dialog.showDialog("Exception", "Exception while retrieving sales");
            System.out.printf("%s %s%n", e.getClass().getName(), e.getMessage());
        }
        return listOfSales;
    }

    public boolean addExpense(Expenses expenses){
        boolean isAdded;
        try {
            MongoClient mongodbClient = MongoClients.create("mongodb://localhost:27017");
            MongoDatabase database = mongodbClient.getDatabase(DB_NAME);
            MongoCollection<Document> collection = database.getCollection("Expenses");
            Document document = new Document("Date", formatDate(expenses.getDate())).append("cost", Integer.parseInt(expenses.getCost()))
                    .append("expenseName", expenses.getExpenseName()).append("expenseID", ThreadLocalRandom.
                            current().nextInt(11111, 99999));
            collection.insertOne(document);
            isAdded = true;
        }catch (Exception e){
            dialog.showDialog("Exception", "Exception while adding "+expenses.getExpenseName());
            isAdded = false;
        }
        return isAdded;
    }

    public ObservableList<Expenses> getAllExpenses(){
        ObservableList<Expenses> expensesList = FXCollections.observableArrayList();
        try {
            MongoClient mongodbClient = MongoClients.create("mongodb://localhost:27017");
            MongoDatabase database = mongodbClient.getDatabase(DB_NAME);
            System.out.println("DATABASE CONN -> SUCCESS -> EXPENSES");
            MongoCollection<Document> collection = database.getCollection("Expenses");
            FindIterable<Document> documents = collection.find();
            int i = 1;
            for (Document doc : documents){
                expensesList.add(new Expenses(
                        String.valueOf(i),
                        String.valueOf(doc.get("expenseID")),
                        (String) doc.get("Date"),
                        (String) doc.get("expenseName"),
                        String.valueOf(doc.get("cost"))
                ));
                ++i;
            }
        }catch (Exception e){
            dialog.showDialog("Exception", "Exception while retrieving expense data");
        }
        return expensesList;
    }

    public ObservableList<Expenses> getExpensesByDate(String date){
        ObservableList<Expenses> expensesList = FXCollections.observableArrayList();
        try {
            MongoClient mongodbClient = MongoClients.create("mongodb://localhost:27017");
            MongoDatabase database = mongodbClient.getDatabase(DB_NAME);
            System.out.println("DATABASE CONN -> SUCCESS -> EXPENSES");
            MongoCollection<Document> collection = database.getCollection("Expenses");
            FindIterable<Document> documents = collection.find(Filters.eq("Date", formatDate(date)));
            int i;
            i = 1;
            for (Document doc : documents){
                expensesList.add(new Expenses(
                        String.valueOf(i),
                        String.valueOf(doc.get("expenseID")),
                        (String) doc.get("Date"),
                        (String) doc.get("expenseName"),
                        String.valueOf(doc.get("cost"))
                ));
                ++i;
            }
        }catch (Exception e){
            dialog.showDialog("Exception", "Exception while retrieving expense data");
        }
        return expensesList;
    }

    public boolean deleteExpense(String expenseID){
        boolean isDeleted;
        try {
            MongoClient mongodbClient = MongoClients.create("mongodb://localhost:27017");
            MongoDatabase database = mongodbClient.getDatabase(DB_NAME);
            MongoCollection<Document> collection = database.getCollection("Expenses");
            collection.deleteOne(Filters.eq("expenseID", Integer.parseInt(expenseID)));
            isDeleted = true;
        }catch (Exception e){
            dialog.showDialog("Exception", "Exception while deleting "+expenseID);
            isDeleted = false;
        }
        return isDeleted;
    }

    public int getRevenueOfTask(String collectionName, String fieldName, String date){
        int total = 0;
        try {
            MongoClient mongodbClient = MongoClients.create("mongodb://localhost:27017");
            MongoDatabase database = mongodbClient.getDatabase(DB_NAME);
            System.out.println("DATABASE CONN -> SUCCESS route -> Expenses");
            MongoCollection<Document> collection = database.getCollection(collectionName);
            if (date.isEmpty()){
                FindIterable<Document> documents = collection.find();
                for (Document doc : documents)
                    total += Integer.parseInt(String.valueOf(doc.get(fieldName)));
            }
            if (!date.isEmpty()){
                FindIterable<Document> documents = collection.find(Filters.eq("Date", formatDate(date)));
                for (Document doc : documents)
                    total += Integer.parseInt(String.valueOf(doc.get(fieldName)));
            }
        }catch (Exception e){
            System.out.printf("%s %s%n", e.getClass().getName(), e.getMessage());
        }
        return total;
    }

    public int getTotalRevenue(String date){
        int salesRevenue = getTotalSales(date);
        int totalExpenses = getRevenueOfTask("Expenses", "cost", date);

        return salesRevenue - totalExpenses;
    }

    public int getTotalCostOfProducts(){
        int total = 0;
        try {
            MongoClient mongodbClient = MongoClients.create("mongodb://localhost:27017");
            MongoDatabase database = mongodbClient.getDatabase(DB_NAME);
            System.out.println("DATABASE CONN -> SUCCESS route -> Product");
            MongoCollection<Document> collection = database.getCollection("Product");
            FindIterable<Document> documents = collection.find();
            for (Document doc : documents)
                total += Integer.parseInt(String.valueOf(doc.get("cost"))) *
                        Integer.parseInt(String.valueOf(doc.get("quantity")));
            System.out.println(total);
        }catch (Exception e){
            System.out.printf("%s %s%n", e.getClass().getName(), e.getMessage());
        }
        return total;
    }

    public double getProfitProgress(String date){
        double totalRevenue = getTotalSales(date);
        double cost = getTotalCostOfProducts() + getRevenueOfTask("Expenses", "cost", date);
        double profitProgress = totalRevenue / cost;
        System.out.println("Total Revenue "+totalRevenue);
        System.out.println("Total cost "+cost);
        System.out.println("Progress "+profitProgress);
        return profitProgress;
    }

    public ArrayList<Integer> getInventoryDailyData(String date){
        ArrayList<Integer> inventoryDailyData = new ArrayList<>();
        try {
            MongoClient mongodbClient = MongoClients.create("mongodb://localhost:27017");
            MongoDatabase database = mongodbClient.getDatabase(DB_NAME);
            MongoCollection<Document> collection = database.getCollection("Sales");
            FindIterable<Document> documents = collection.find(Filters.eq("Date", formatDate(date)));
            int remainingProducts = getCollectionCount("Product");
            int totalSales = getTotalSales(date);
            int amountReceived = 0;
            int profit = 0;
            for (Document doc : documents){
                amountReceived += Integer.parseInt(String.valueOf(doc.get("price")));
                profit += Integer.parseInt(String.valueOf(doc.get("profit")));
            }
            inventoryDailyData.add(remainingProducts);
            inventoryDailyData.add(totalSales);
            inventoryDailyData.add(amountReceived);
            inventoryDailyData.add(profit);
        }catch (Exception e){
            System.out.printf("%s %s%n", e.getClass().getName(), e.getMessage());
        }
        return inventoryDailyData;
    }

    public ObservableList<User> getUsers(){
        ObservableList<User> users = FXCollections.observableArrayList();
        try {
            MongoClient mongodbClient = MongoClients.create("mongodb://localhost:27017");
            MongoDatabase database = mongodbClient.getDatabase(DB_NAME);
            MongoCollection<Document> collection = database.getCollection("Users");
            FindIterable<Document> documents = collection.find();
            int i = 1;
            for (Document doc : documents){
                users.add(new User(String.valueOf(i), (String) doc.get("username"), (String) doc.get("phoneNo"), ""));
                ++i;
            }
        }catch (Exception e){
            dialog.showDialog("Exception", "Exception while retrieving users from database");
            System.out.printf("%s %s%n", e.getClass().getName(), e.getMessage());
        }
        return users;
    }
    public ArrayList<String> getUser(String username){
        ArrayList<String> userList = new ArrayList<>();
        try {
            MongoClient mongodbClient = MongoClients.create("mongodb://localhost:27017");
            MongoDatabase database = mongodbClient.getDatabase(DB_NAME);
            MongoCollection<Document> collection = database.getCollection("Users");
            FindIterable<Document> documents = collection.find(Filters.eq("username", username));
            for (Document doc : documents){
                userList.add((String) doc.get("username"));
                userList.add((String) doc.get("salt"));
                userList.add((String) doc.get("password"));
            }
        }catch (Exception e){
            dialog.showDialog("Error", "Error retrieving user data");
            System.out.printf("%s %s%n", e.getClass().getName(), e.getMessage());
        }
        return userList;
    }
    String salt = null;
    private static String getSalt() throws NoSuchAlgorithmException{
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return Arrays.toString(salt);
    }
    public static String generateStrongPassword(String password, String salt) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        byte[] hashedPassword = digest.digest((password + salt).getBytes());
        return toHexString(hashedPassword);
    }

    public void addUser(String username, String password, String phoneNo){
        try {
            MongoClient mongodbClient = MongoClients.create("mongodb://localhost:27017");
            MongoDatabase database = mongodbClient.getDatabase(DB_NAME);
            System.out.println("DATABASE CONN -> [SUCCESS] -> Users");
            MongoCollection<Document> collection = database.getCollection("Users");
            createUniqueIndex("username", "Users");
            salt = getSalt();
            Document document = new Document("username", username).append("salt", salt).
                    append("password", generateStrongPassword(password, salt)).append("phoneNo", phoneNo);
            try {
                collection.insertOne(document);
                dialog.showDialog("Success", username+" user inserted successfully");
            }catch (MongoWriteException e){
                dialog.showDialog("Error", "user with name "+username+" already exists");
            }
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
    public boolean validatePassword(String loginPassword, String hashPassword, String salt) throws NoSuchAlgorithmException{
        String calcHash = generateStrongPassword(loginPassword, salt);
        return calcHash.equals(hashPassword);
    }

    public static String toHexString(byte[] bytes){
        StringBuilder sb = new StringBuilder();
        for (byte aByte : bytes){
            sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }

    public boolean updatePassword(String username, String newPassword){
        ArrayList<String> user = new ArrayList<>();
        boolean isChanged;
        try {
            MongoClient mongodbClient = MongoClients.create("mongodb://localhost:27017");
            MongoDatabase database = mongodbClient.getDatabase(DB_NAME);
            MongoCollection<Document> collection = database.getCollection("Users");
            System.out.println("DATABASE CONN -> [SUCCESS] -> ROUTE -> USERS");
            FindIterable<Document> documents = collection.find(Filters.eq("username", username));
            for (Document doc : documents)
                user.add((String) doc.get("username"));
            salt = getSalt();
            if (!user.isEmpty()){
                collection.updateOne(Filters.eq("username", username), Updates.set("salt", salt));
                collection.updateOne(Filters.eq("username", username),
                        Updates.set("password", generateStrongPassword(newPassword, salt)));
                isChanged = true;
            }else {
                dialog.showDialog("Not Found!", "User with name "+username+" not found!");
                isChanged = false;
            }
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return isChanged;
    }

    public boolean addBusiness(String businessName, String tagline, String address, String username
    , String phoneNo, String password){
        boolean isWrite = false;
        try {
            MongoClient mongodbClient = MongoClients.create("mongodb://localhost:27017");
            MongoDatabase database = mongodbClient.getDatabase(DB_NAME);
            MongoCollection<Document> collection = database.getCollection("Business");
            createUniqueIndex("name", "Business");
            Document document = new Document("name", businessName).append("tagline", tagline)
                    .append("address", address).append("phoneNo", phoneNo);
            createUniqueIndex("name", "Business");
            try {
                addUser(username, password, phoneNo);
                collection.insertOne(document);
                isWrite = true;
            }catch (MongoWriteException e){
                dialog.showDialog("Exception", "Business with name already exists");
            }
        }catch (Exception e){
            dialog.showDialog("Exception", "Exception while writing "+businessName+" to database");
            System.out.printf("%s %s%n", e.getClass().getName(), e.getMessage());
        }
        return isWrite;
    }

    public boolean deleteTask(String fieldName, String key, String collectionName){
        boolean isDeleted;
        try {
            MongoClient mongodbClient = MongoClients.create("mongodb://localhost:27017");
            MongoDatabase database = mongodbClient.getDatabase(DB_NAME);
            MongoCollection<Document> collection = database.getCollection(collectionName);
            collection.deleteOne(Filters.eq(fieldName, key));
            isDeleted = true;
        }catch (Exception e){
            isDeleted = false;
            System.out.printf("%s %s%n", e.getClass().getName(), e.getMessage());
        }
        return isDeleted;
    }

    public ArrayList<String> getBusinessInfo(){
        ArrayList<String> businessInfo = new ArrayList<>();
        try {
            MongoClient mongodbClient = MongoClients.create("mongodb://localhost:27017");
            MongoDatabase database = mongodbClient.getDatabase(DB_NAME);
            MongoCollection<Document> collection = database.getCollection("Business");
            FindIterable<Document> documents = collection.find();
            for (Document doc : documents){
                businessInfo.add((String) doc.get("name"));
                businessInfo.add((String) doc.get("tagline"));
                businessInfo.add((String) doc.get("address"));
                businessInfo.add((String) doc.get("phoneNo"));
            }
        }catch (Exception e){
            dialog.showDialog("Exception", "Error retrieving business information");
            System.out.printf("%s %s%n", e.getClass().getName(), e.getMessage());
        }
        return businessInfo;
    }

    public boolean updateBusinessInfo(String name, String tagline, String address, String tel){
        boolean isInsert;
        try {
            MongoClient mongodbClient = MongoClients.create("mongodb://localhost:27017");
            MongoDatabase database = mongodbClient.getDatabase(DB_NAME);
            MongoCollection<Document> collection = database.getCollection("Business");
            collection.deleteOne(Filters.eq("name", getBusinessInfo().get(0)));
            Document document = new Document("name", name).append("tagline", tagline).append("address", address)
                    .append("phoneNo", tel);
            collection.insertOne(document);
            isInsert = true;
        }catch (Exception e){
            dialog.showDialog("Error", "Error occurred while updating "+getBusinessInfo().get(0));
            isInsert = false;
        }
        return isInsert;
    }

    public ArrayList<String> getSale(int saleID){
        ArrayList<String> saleInfo = new ArrayList<>();
        try {
            MongoClient mongodbClient = MongoClients.create("mongodb://localhost:27017");
            MongoDatabase database = mongodbClient.getDatabase(DB_NAME);
            MongoCollection<Document> collection = database.getCollection("Sales");
            FindIterable<Document> documents = collection.find(Filters.eq("saleID", saleID));
            for (Document doc : documents){
                saleInfo.add(String.valueOf(doc.get("saleID")));
                saleInfo.add((String) doc.get("code"));
                saleInfo.add((String) doc.get("name"));
                saleInfo.add(String.valueOf(doc.get("quantity")));
                saleInfo.add(String.valueOf(doc.get("price")));
                saleInfo.add((String) doc.get("Date"));
                saleInfo.add(String.valueOf(doc.get("profit")));
            }
        }catch (Exception e){
            dialog.showDialog("Exception", "Exception occurred while retrieving sale no "+saleID+" details");
            System.out.printf("%s %s%n", e.getClass().getName(), e.getMessage());
        }
        return saleInfo;
    }

    public boolean updateSale(int saleID, int cost){
        boolean onUpdate;
        try {
            MongoClient mongodbClient = MongoClients.create("mongodb://localhost:27017");
            MongoDatabase database = mongodbClient.getDatabase(DB_NAME);
            MongoCollection<Document> collection = database.getCollection("Sales");
            collection.updateOne(Filters.eq("saleID", saleID), Updates.set("profit", -1 * cost));
            onUpdate = true;
        }catch (Exception e){
            dialog.showDialog("Exception", "Exception while updating sale no "+saleID);
            System.out.printf("%s %s%n", e.getClass().getName(), e.getMessage());
            onUpdate = false;
        }
        return onUpdate;
    }
}