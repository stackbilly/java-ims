package com.stacklink.inventory_management_system;

import com.mongodb.MongoWriteException;
import com.mongodb.client.FindIterable;
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

import java.math.BigInteger;
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
        com.mongodb.client.MongoClient mongodbClient = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase database = mongodbClient.getDatabase(DB_NAME);
        MongoCollection<Document> collection = database.getCollection(collectionName);
        collection.createIndex(index, new IndexOptions().unique(true));
    }

    public boolean addCategory(String categoryName, String status) {
        try {
            com.mongodb.client.MongoClient mongodbClient = MongoClients.create("mongodb://localhost:27017");
            MongoDatabase database = mongodbClient.getDatabase(DB_NAME);
            System.out.println("Connection Status: [OK], Database name: " + database.getName());

            String collectionName = "Category";

            MongoCollection<Document> collection = database.getCollection(collectionName);

            createUniqueIndex("name", collectionName);

            Document document = new Document("name", categoryName).append("status", status);
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
            com.mongodb.client.MongoClient mongodbClient = MongoClients.create("mongodb://localhost:27017");
            MongoDatabase database = mongodbClient.getDatabase(DB_NAME);
            System.out.println("Connection Status: [OK], Database name: " + database.getName());

            MongoCollection<Document> collection = database.getCollection(collectionName);
            FindIterable<Document> documents = collection.find();
            int i = 1;
            for (Document doc : documents) {
                categories.add(new Category(String.valueOf(i), (String) doc.get("name"), (String) doc.get("status")));
                i++;
            }
        } catch (Exception e) {
            dialog.showDialog("Exception", "Error occurred while retrieving data");
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return categories;
    }

    public ArrayList<String> getCategoryData() {
        ArrayList<String> categories = new ArrayList<>();
        String collectionName = "Category";
        try {
            com.mongodb.client.MongoClient mongodbClient = MongoClients.create("mongodb://localhost:27017");
            MongoDatabase database = mongodbClient.getDatabase(DB_NAME);
            System.out.println("Connection Status: [OK], Database name: " + database.getName());

            MongoCollection<Document> collection = database.getCollection(collectionName);
            FindIterable<Document> documents = collection.find();
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
            com.mongodb.client.MongoClient mongodbClient = MongoClients.create("mongodb://localhost:27017");
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
            com.mongodb.client.MongoClient mongodbClient = MongoClients.create("mongodb://localhost:27017");
            MongoDatabase database = mongodbClient.getDatabase(DB_NAME);
            System.out.println("Database connection: [GOOD STATE], /route -> " + collectionName);

            MongoCollection<Document> collection = database.getCollection(collectionName);
            FindIterable<Document> documents = collection.find();
            int i = 1;
            for (Document doc : documents) {
                products.add(new Product(String.valueOf(i), (String) doc.get("name"), (String) doc.get("description"), (String) doc.get("code"),
                        (String) doc.get("category"), (Integer) doc.get("quantity"), (Integer) doc.get("cost"),
                        (Integer) doc.get("sale"), String.valueOf(doc.get("date")), (Integer)
                        doc.get("total"), (Integer) doc.get("expectedSales")));
                i++;
            }
        } catch (Exception e) {
            dialog.showDialog("Exception", "Exception while extracting data from database!");
            e.printStackTrace();
        }
        return products;
    }

    public boolean updateProduct(String collectionName, String fieldName, String oldValue, String newValue) {
        boolean isUpdate = false;
        try {
            com.mongodb.client.MongoClient mongodbClient = MongoClients.create("mongodb://localhost:27017");
            MongoDatabase database = mongodbClient.getDatabase(DB_NAME);

            MongoCollection<Document> collection = database.getCollection(collectionName);
            collection.updateOne(Filters.eq(fieldName, oldValue), Updates.set(fieldName, newValue));
            isUpdate = true;
            System.out.println(fieldName + " update state: ->/ [GOOD]");
        } catch (MongoWriteException e) {
            System.out.println(fieldName + " update state: ->/ [BAD]");
            e.printStackTrace();
        }
        return isUpdate;
    }

    public boolean updateProduct(String collectionName, String fieldName, int oldValue, int newValue) {
        boolean isUpdate = false;
        try {
            com.mongodb.client.MongoClient mongodbClient = MongoClients.create("mongodb://localhost:27017");
            MongoDatabase database = mongodbClient.getDatabase(DB_NAME);

            MongoCollection<Document> collection = database.getCollection(collectionName);
            collection.updateOne(Filters.eq(fieldName, oldValue), Updates.set(fieldName, newValue));
            isUpdate = true;
            System.out.println(fieldName + " update state: ->/ [GOOD]");
        } catch (MongoWriteException e) {
            System.out.println(fieldName + " update state: ->/ [BAD]");
            e.printStackTrace();
        }
        return isUpdate;
    }

    public void updateProduct(String productName, int quantity) {
        try {
            com.mongodb.client.MongoClient mongodbClient = MongoClients.create("mongodb://localhost:27017");
            MongoDatabase database = mongodbClient.getDatabase(DB_NAME);

            MongoCollection<Document> collection = database.getCollection("Product");
            FindIterable<Document> documents = collection.find(Filters.eq("name", productName));
            int mQuantity = 0;
            for (Document doc : documents)
                mQuantity = Integer.parseInt(String.valueOf(doc.get("quantity")));
            collection.updateOne(Filters.eq("name", productName), Updates.set("quantity", mQuantity + quantity));
            System.out.println(productName + " update state: ->/ [GOOD]");
        } catch (MongoWriteException e) {
            System.out.println(productName + " update state: ->/ [BAD]");
            e.printStackTrace();
        }
    }

    public ArrayList<String> getSaleData(String code) {
        ArrayList<String> saleData = new ArrayList<>();
        try {
            com.mongodb.client.MongoClient mongodbClient = MongoClients.create("mongodb://localhost:27017");
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
            e.printStackTrace();
        }
        return saleData;
    }

    public boolean addSaleData(Cart cart, String getCost) {
        boolean isInserted = false;
        try {
            com.mongodb.client.MongoClient mongodbClient = MongoClients.create("mongodb://localhost:27017");
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
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isInserted;
    }

    public ObservableList<Cart> getSaleData() {
        ObservableList<Cart> saleData = FXCollections.observableArrayList();
        try {
            com.mongodb.client.MongoClient mongodbClient = MongoClients.create("mongodb://localhost:27017");
            MongoDatabase database = mongodbClient.getDatabase(DB_NAME);

            MongoCollection<Document> collection = database.getCollection("Sales");

            FindIterable<Document> documents = collection.find(Filters.eq("status", "notSold"));
            for (Document doc : documents)
                saleData.add(new Cart(String.valueOf(doc.get("saleID")), (String) doc.get("code"), (String) doc.get("name"),
                        String.valueOf(doc.get("quantity")), String.valueOf(doc.get("price")), String.valueOf(doc.get("total")),
                        String.valueOf(doc.get("date"))));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return saleData;
    }

    public void deleteCartItem(int saleID) {
        try {
            com.mongodb.client.MongoClient mongodbClient = MongoClients.create("mongodb://localhost:27017");
            MongoDatabase database = mongodbClient.getDatabase(DB_NAME);

            MongoCollection<Document> collection = database.getCollection("Sales");
            collection.deleteOne(Filters.eq("saleID", saleID));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addPaySlip(PaySlip paySlip) {
        try {
            com.mongodb.client.MongoClient mongodbClient = MongoClients.create("mongodb://localhost:27017");
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
            e.printStackTrace();
        }
    }

    public int getProductQuantity(String productName) {
        int quantity = 0;
        try {
            com.mongodb.client.MongoClient mongodbClient = MongoClients.create("mongodb://localhost:27017");
            MongoDatabase database = mongodbClient.getDatabase(DB_NAME);
            System.out.println("Database CONN -> [SUCCESS] -route -> Product");

            MongoCollection<Document> collection = database.getCollection("Product");
            FindIterable<Document> documents = collection.find(Filters.eq("name", productName));
            for (Document doc : documents)
                quantity = Integer.parseInt(String.valueOf(doc.get("quantity")));

        } catch (Exception e) {
            dialog.showDialog("Exception", "Error completing transaction");
            e.printStackTrace();
        }
        return quantity;
    }

    public void updateProductOnSale(String code, int quantity) {
        int newQuantity;
        try {
            com.mongodb.client.MongoClient mongodbClient = MongoClients.create("mongodb://localhost:27017");
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
            e.printStackTrace();
        }
    }

    public void updateSellState() {
        try {
            com.mongodb.client.MongoClient mongodbClient = MongoClients.create("mongodb://localhost:27017");
            MongoDatabase database = mongodbClient.getDatabase(DB_NAME);
            System.out.println("Database CONN -> [SUCCESS] -route -> Sales");

            MongoCollection<Document> collection = database.getCollection("Sales");

            collection.updateMany(Filters.eq("status",
                    "notSold"), Updates.set("status", "Sold"));

        } catch (Exception e) {
            dialog.showDialog("Exception", "Error completing transaction");
            e.printStackTrace();
        }
    }

    public int getCollectionCount(String collectionName) {
        int noOfProducts = 0;
        try {
            com.mongodb.client.MongoClient mongodbClient = MongoClients.create("mongodb://localhost:27017");
            MongoDatabase database = mongodbClient.getDatabase(DB_NAME);
            System.out.println("Database CONN -> [SUCCESS] -route -> Products");
            MongoCollection<Document> collection = database.getCollection(collectionName);
            noOfProducts = (int) collection.countDocuments();
        } catch (Exception e) {
            dialog.showDialog("Exception", "Error reading the database");
            e.printStackTrace();
        }
        return noOfProducts;
    }

    public int getTotalSales(String date) {
        int total = 0;
        try {
            com.mongodb.client.MongoClient mongodbClient = MongoClients.create("mongodb://localhost:27017");
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
            e.printStackTrace();
        }
        return total;
    }

    public ObservableList<Sales> getAllSales() {
        ObservableList<Sales> listOfSales = FXCollections.observableArrayList();
        try {
            com.mongodb.client.MongoClient mongodbClient = MongoClients.create("mongodb://localhost:27017");
            MongoDatabase database = mongodbClient.getDatabase(DB_NAME);
            MongoCollection<Document> collection = database.getCollection("Sales");
            FindIterable<Document> documents = collection.find(Filters.eq("status", "Sold"));
            int i = 1;
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
        } catch (Exception e) {
            dialog.showDialog("Exception", "Exception while retrieving sales");
            e.printStackTrace();
        }
        return listOfSales;
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
            com.mongodb.client.MongoClient mongodbClient = MongoClients.create("mongodb://localhost:27017");
            MongoDatabase database = mongodbClient.getDatabase(DB_NAME);
            MongoCollection<Document> collection = database.getCollection("Sales");
            Bson statusFilter = Filters.eq("status", "Sold");
            Bson dateFilter = Filters.eq("Date", formatDate(date));
            FindIterable<Document> documents = collection.find(Filters.and(statusFilter, dateFilter));
            int i = 1;
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
        } catch (Exception e) {
            dialog.showDialog("Exception", "Exception while retrieving sales");
            e.printStackTrace();
        }
        return listOfSales;
    }

    public ObservableList<Sales> getSalesBySaleID(String saleID) {
        ObservableList<Sales> listOfSales = FXCollections.observableArrayList();
        try {
            com.mongodb.client.MongoClient mongodbClient = MongoClients.create("mongodb://localhost:27017");
            MongoDatabase database = mongodbClient.getDatabase(DB_NAME);
            MongoCollection<Document> collection = database.getCollection("Sales");
            Bson statusFilter = Filters.eq("status", "Sold");
            Bson saleIDFilter = Filters.eq("saleID", Integer.parseInt(saleID));
            FindIterable<Document> documents = collection.find(Filters.and(statusFilter, saleIDFilter));
            if (saleID.length() > 1) {
                int i = 1;
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
            if (saleID.length() < 1) {
                listOfSales = getAllSales();
            }
        } catch (Exception e) {
            dialog.showDialog("Exception", "Exception while retrieving sales");
            e.printStackTrace();
        }
        return listOfSales;
    }

    public boolean repairedPhoneData(Phone phone) {
        boolean isInserted;
        try {
            com.mongodb.client.MongoClient mongodbClient = MongoClients.create("mongodb://localhost:27017");
            MongoDatabase database = mongodbClient.getDatabase(DB_NAME);
            System.out.println("Database CONN: -> Phones");
            MongoCollection<Document> collection = database.getCollection("Phones");
            Document document = new Document("Date",LocalDate.now().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)))
                    .append("phone name", phone.getPhoneName()).append("imei", phone.getImei())
                    .append("problem", phone.getProblem()).append("cost", Integer.parseInt(phone.getCost())).
                    append("paid", Integer.parseInt(phone.getPaid())).append("profit", Integer.parseInt(phone.getProfit()))
                    .append("vendor", phone.getVendor());
            collection.insertOne(document);
            isInserted = true;
        } catch (Exception e) {
            dialog.showDialog("Exception", "Exception while adding " + phone.getPhoneName() + " to database");
            e.printStackTrace();
            isInserted = false;
        }
        return isInserted;
    }

    public ObservableList<Phone> getPhones(){
        ObservableList<Phone> phoneObservableList = FXCollections.observableArrayList();
        try {
            com.mongodb.client.MongoClient mongodbClient = MongoClients.create("mongodb://localhost:27017");
            MongoDatabase database = mongodbClient.getDatabase(DB_NAME);
            System.out.println("DATABASE CONN -> Phones");
            MongoCollection<Document> collection = database.getCollection("Phones");
            FindIterable<Document> documents = collection.find();
            for (Document doc : documents){
                int i = 1;
                phoneObservableList.add(new Phone(
                        String.valueOf(i),
                        (String) doc.get("Date"),
                        (String) doc.get("phone name"),
                        (String) doc.get("imei"),
                        (String) doc.get("problem"),
                        String.valueOf(doc.get("cost")),
                        String.valueOf(doc.get("paid")),
                        String.valueOf(doc.get("profit")),
                        (String) doc.get("vendor")
                ));
                ++i;
            }
        }catch (Exception e){
            dialog.showDialog("Exception", "Error while adding phone to database");
            e.printStackTrace();
        }
        return phoneObservableList;
    }

    public ObservableList<Phone> getPhonesByDate(String date) {
        ObservableList<Phone> listOfPhones = FXCollections.observableArrayList();
        try {
            com.mongodb.client.MongoClient mongodbClient = MongoClients.create("mongodb://localhost:27017");
            MongoDatabase database = mongodbClient.getDatabase(DB_NAME);
            MongoCollection<Document> collection = database.getCollection("Phones");
            Bson dateFilter = Filters.eq("Date", formatDate(date));
            FindIterable<Document> documents = collection.find(Filters.and(dateFilter));
            int i = 1;
            for (Document doc : documents) {
                listOfPhones.add(new Phone(
                        String.valueOf(i),
                        (String) doc.get("Date"),
                        (String) doc.get("phone name"),
                        (String) doc.get("imei"),
                        (String) doc.get("problem"),
                        String.valueOf(doc.get("cost")),
                        String.valueOf(doc.get("paid")),
                        String.valueOf(doc.get("profit")),
                        (String) doc.get("vendor")
                ));
                ++i;
            }
        } catch (Exception e) {
            dialog.showDialog("Exception", "Exception while retrieving sales");
            e.printStackTrace();
        }
        return listOfPhones;
    }

    public ObservableList<Phone> getPhonesByIMEI(String imei) {
        ObservableList<Phone> listOfPhones = FXCollections.observableArrayList();
        try {
            com.mongodb.client.MongoClient mongodbClient = MongoClients.create("mongodb://localhost:27017");
            MongoDatabase database = mongodbClient.getDatabase(DB_NAME);
            MongoCollection<Document> collection = database.getCollection("Phones");
            FindIterable<Document> documents = collection.find(Filters.eq("imei", imei));
            int i = 1;
            for (Document doc : documents) {
                listOfPhones.add(new Phone(
                        String.valueOf(i),
                        (String) doc.get("Date"),
                        (String) doc.get("phone name"),
                        (String) doc.get("imei"),
                        (String) doc.get("problem"),
                        String.valueOf(doc.get("cost")),
                        String.valueOf(doc.get("paid")),
                        String.valueOf(doc.get("profit")),
                        (String) doc.get("vendor")
                ));
                ++i;
            }
        } catch (Exception e) {
            dialog.showDialog("Exception", "Exception while retrieving sales");
            e.printStackTrace();
        }
        return listOfPhones;
    }

    public boolean addExpense(Expenses expenses){
        boolean isAdded;
        try {
            com.mongodb.client.MongoClient mongodbClient = MongoClients.create("mongodb://localhost:27017");
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
            com.mongodb.client.MongoClient mongodbClient = MongoClients.create("mongodb://localhost:27017");
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
            com.mongodb.client.MongoClient mongodbClient = MongoClients.create("mongodb://localhost:27017");
            MongoDatabase database = mongodbClient.getDatabase(DB_NAME);
            System.out.println("DATABASE CONN -> SUCCESS -> EXPENSES");
            MongoCollection<Document> collection = database.getCollection("Expenses");
            FindIterable<Document> documents = collection.find(Filters.eq("Date", formatDate(date)));
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

    public boolean deleteExpense(String expenseID){
        boolean isDeleted;
        try {
            com.mongodb.client.MongoClient mongodbClient = MongoClients.create("mongodb://localhost:27017");
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
            com.mongodb.client.MongoClient mongodbClient = MongoClients.create("mongodb://localhost:27017");
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
            e.printStackTrace();
        }
        return total;
    }

    public int getTotalRevenue(String date){
        int salesRevenue = getTotalSales(date);
        int phoneRevenue = getRevenueOfTask("Phones", "profit", date);
        int totalExpenses = getRevenueOfTask("Expenses", "cost", date);

        return (salesRevenue + phoneRevenue) - totalExpenses;
    }

    public int getTotalCostOfProducts(){
        int total = 0;
        try {
            com.mongodb.client.MongoClient mongodbClient = MongoClients.create("mongodb://localhost:27017");
            MongoDatabase database = mongodbClient.getDatabase(DB_NAME);
            System.out.println("DATABASE CONN -> SUCCESS route -> Product");
            MongoCollection<Document> collection = database.getCollection("Product");
            FindIterable<Document> documents = collection.find();
            for (Document doc : documents)
                total += Integer.parseInt(String.valueOf(doc.get("cost"))) *
                        Integer.parseInt(String.valueOf(doc.get("quantity")));
            System.out.println(total);
        }catch (Exception e){
            e.printStackTrace();
        }
        return total;
    }

    public double getProfitProgress(String date){
        double totalRevenue = getTotalSales(date) + getRevenueOfTask("Phones", "profit", date);
        double cost = getTotalCostOfProducts() + getRevenueOfTask("Expenses", "cost", date);
        double profitProgress = totalRevenue / cost;
        System.out.println("Total Revenue "+totalRevenue);
        System.out.println("Total cost "+cost);
        System.out.println("Progress "+profitProgress);
        return profitProgress;
    }

    public ArrayList<Integer> getDailyPhoneRepairedData(String date){
        ArrayList<Integer> dailyPhoneRepData = new ArrayList<>();
        try {
            com.mongodb.client.MongoClient mongodbClient = MongoClients.create("mongodb://localhost:27017");
            MongoDatabase database = mongodbClient.getDatabase(DB_NAME);
            System.out.println("DATABASE CONN -> [SUCCESS] / route -> [Daily Phone Repaired Data]");
            MongoCollection<Document> collection = database.getCollection("Phones");
            FindIterable<Document> documents = collection.find(Filters.eq("Date", formatDate(date)));
            int count = 0;
            int amountSpent = 0;
            int amountReceived = 0;
            int profit = 0;
            for (Document doc : documents)
            {
                amountSpent += Integer.parseInt(String.valueOf(doc.get("cost")));
                amountReceived += Integer.parseInt(String.valueOf(doc.get("paid")));
                profit += Integer.parseInt(String.valueOf(doc.get("profit")));
                ++count;
            }
            dailyPhoneRepData.add(count);
            dailyPhoneRepData.add(amountSpent);
            dailyPhoneRepData.add(amountReceived);
            dailyPhoneRepData.add(profit);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
        return dailyPhoneRepData;
    }

    public ArrayList<Integer> getInventoryDailyData(String date){
        ArrayList<Integer> inventoryDailyData = new ArrayList<>();
        try {
            com.mongodb.client.MongoClient mongodbClient = MongoClients.create("mongodb://localhost:27017");
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
            e.printStackTrace();
        }
        return inventoryDailyData;
    }

    public ArrayList<String> getUser(String username){
        ArrayList<String> userList = new ArrayList<>();
        try {
            com.mongodb.client.MongoClient mongodbClient = MongoClients.create("mongodb://localhost:27017");
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
            e.printStackTrace();
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
        String calculatedHash = toHexString(hashedPassword);
        return calculatedHash;
    }

    public void addUser(String username, String password){
        try {
            com.mongodb.client.MongoClient mongodbClient = MongoClients.create("mongodb://localhost:27017");
            MongoDatabase database = mongodbClient.getDatabase(DB_NAME);
            System.out.println("DATABASE CONN -> [SUCCESS] -> Users");
            MongoCollection<Document> collection = database.getCollection("Users");
            createUniqueIndex("username", "Users");
            salt = getSalt();
            Document document = new Document("username", username).append("salt", salt).
                    append("password", generateStrongPassword(password, salt));
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
}