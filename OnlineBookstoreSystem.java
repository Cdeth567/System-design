/*
Simulates an online bookstore with book/user management, 
subscriptions, and access control. Uses Factory for users, Proxy for book access, 
and Observer for price notifications. Handles duplicates and invalid subscription 
actions with error messages. Supports creating books/users, subscribing/unsubscribing, 
updating prices, reading, and listening to books.
*/

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String[] s = scanner.nextLine().split(" ");
        OnlineBookStore onlineBookStore = OnlineBookStore.getInstance(); // create unique instance of OnlineBookStore class
        while (!s[0].equals("end")) {
            switch (s[0]) {
                case "createBook":
                    if (!onlineBookStore.findTitle(s[1])) { // if book with this title isn't created yet
                        Book book = new Book(s[1], s[2], s[3]); // create book
                        onlineBookStore.addBook(s[1]); // add title of the book to list
                        onlineBookStore.addBookToMap(book, s[1]); // add book to bookMap
                    }
                    break;
                case "createUser":
                    if (!onlineBookStore.findUserName(s[2])) { //if user with this name isn't created yet
                        UserFactory userFactory = createUserFactory(s[1]); // create user using Factory

                        onlineBookStore.addName(s[2]); // add name of the user to list of names
                        onlineBookStore.addUserToMap(userFactory.createUser(s[2]), s[2]); // add user to UserMap
                    }
                    break;
                case "updatePrice":
                    onlineBookStore.notifyObservers(s[2], s[1]); // notify users about changing price of the book
                    break;
                case "readBook":
                    onlineBookStore.getUser(s[1]).readBook(onlineBookStore.getBook(s[2])); // reading book by the user
                    break;
                case "listenBook":
                    onlineBookStore.getUser(s[1]).listenBook(onlineBookStore.getBook(s[2])); // listening book by the user
                    break;
                case "subscribe":
                    onlineBookStore.getUser(s[1]).subscribe(); // subscribing user to updates
                    onlineBookStore.addSubscribe(onlineBookStore.getUser(s[1])); // add user to subscribed
                    break;
                case "unsubscribe":
                    onlineBookStore.getUser(s[1]).unsubscribe(); // unsubscribe user
                    onlineBookStore.removeSubscribe(onlineBookStore.getUser(s[1])); // remove user from subscribers
                    break;
            }
            s = scanner.nextLine().split(" ");
        }
    }

    static UserFactory createUserFactory(String type) { // method for creating instances of users by type
        if (type.equalsIgnoreCase("standard")) {
            return new StandardUserFactory();
        } else if (type.equalsIgnoreCase("premium")) {
            return new PremiumUserFactory();
        } else {
            throw new RuntimeException(type + "is unknown");
        }
    }
}

class OnlineBookStore implements Observed { // create class OnlineBookStore using Singleton pattern to have only 1 instance
    private static OnlineBookStore unique; // variable of unique instance of OnlineBookStore class
    private ArrayList<String> books; // map of all titles of books
    private ArrayList<String> names; // map of all names of users
    private ArrayList<Observer> subscribedUsers; // list of subscribed users
    private HashMap<String, Book> bookMap; // map of all books and their titles
    private HashMap<String, UserDecorator> userMap; // map of all users and their names
    private OnlineBookStore() { // constructor
        /*
        create new lists and hashMap for private variables
         */
        this.books = new ArrayList<>();
        this.names = new ArrayList<>();
        this.bookMap = new HashMap<>();
        this.userMap = new HashMap<>();
        this.subscribedUsers = new ArrayList<>();
    }

    public static OnlineBookStore getInstance() { // method for making unique instance of BankingSystem
        if (unique == null) // if the instance has not been created yet
            unique = new OnlineBookStore(); // create it
        return unique;
    }

    @Override
    public void addSubscribe(Observer observer) { // add user to list of subscribed users
        if (!subscribedUsers.contains(observer)) this.subscribedUsers.add(observer); // check if the user already in the list and add if no
    }

    @Override
    public void removeSubscribe(Observer observer) { // function to remove user from subscribedUsers
        this.subscribedUsers.remove(observer); // remove user from subscribedUsers
    }

    void addBook(String title) {
        books.add(title); // add title of the book to books
    }

    boolean findTitle(String newTitle) { // function to check if this new title already exists
        for (String t : books) { // check every title in books
            if (t.equals(newTitle)) { // if current title equals new title
                System.out.println("Book already exists"); // output message
                return true; // return true because this new title already exists
            }
        }
        return false; // return false because the newTitle does not exist yet
    }

    void addName(String userName) {
        names.add(userName); // add name to names
    }

    boolean findUserName(String userName) { // function to check if new name already exists
        for (String name : names) { // check every name in names
            if (name.equals(userName)) { // if current name equals new name
                System.out.println("User already exists"); // output message
                return true; // return true because this new name already exists
            }
        }
        return false; // return false because the new name does not exist yet
    }

    void addBookToMap(Book book, String title) { // function to add new book to bookMap
        bookMap.put(title, book);
    }

    void addUserToMap(UserDecorator user, String name) { // function to add new user to userMap
        userMap.put(name, user);
    }

    UserDecorator getUser(String name) { // getter for value of the userMap
        return userMap.get(name);
    }

    Book getBook(String title) { // getter for value of the bookMap
        return bookMap.get(title);
    }

    @Override
    public void notifyObservers(String price, String title) { // function to notify subscribed users about the book price update
        Book book = bookMap.get(title); // take book from bookMap
        if (!(subscribedUsers == null)) { // if subscribedUsers list isn't empty
            /*
            * going through each user in subscribedUsers to notify about update
            * then output the message for each of them
             */
            for (Observer user : subscribedUsers) System.out.println(user.getUserName() + " notified about price update for " + title + " to " + price);
            book.setPrice(price); // set new price for book
        }
    }
}

interface Observer { // interface for using Observer pattern
    String getUserName(); // method for getting name of the observer
}

interface Observed { // another interface for using Observer pattern
    void addSubscribe(Observer observer); // method for adding new Observer to list of subscribed users
    void removeSubscribe(Observer observer); // method for removing Observer from the list of subscribed users
    void notifyObservers(String name, String price); // method for notifying subscribed users about updates
}

class Book {
    private String title;
    private String author;
    private String price;
    boolean isCreated; // variable to check if a book has already been created and not to create it again
    Book(String title, String author, String price) {
        this.title = title;
        this.author = author;
        this.price = price;
        this.isCreated = true;
    }

    String getPrice() { // getter for price of the book
        return price;
    }
    String getTitle() { // getter for title of the book
        return title;
    }

    String getAuthor() { // method for getting author of the book
        return author;
    }

    void setPrice(String newPrice) { // method for setting new price
        this.price = newPrice;
    }
}

interface UserInterface { // interface describing all methods of the User class
    void subscribe(); // method for subscribing to book updates
    void unsubscribe(); // method for unsubscribing from an update about books
    void readBook(Book book); // method for output the message about reading book
    void listenBook(Book book); // method for output the message about listening book
}

class User implements UserInterface, Observer {
    private boolean subscription;
    private String userName;
    public boolean isCreated;
    // subscribe to get notifications about book updates
    @Override
    public void subscribe() { // method for subscribing to book updates
        if (subscription) { // if the user is subscribed to updates
            System.out.println("User already subscribed");  // output message about repeating subscribing
        } else { // if the user is unsubscribed
            subscription = true; // subscribe him
        }
    }
    // Unsubscribes user with the given username from getting notifications about books' prices updates
    @Override
    public void unsubscribe() { // method for unsubscribing from an update about books
        if (subscription) { // if the user is subscribed to updates
            subscription = false; // unsubscribe him
        } else { // if the user is unsubscribed
            System.out.println("User is not subscribed"); // output message about repeating unsubscribing
        }
    }
    User(String userName) {
        this.userName = userName;
        this.subscription = false;
        this.isCreated = true;
    }

    @Override
    public String getUserName() { // getter for name of the user
        return userName;
    } // override getUserName method from Observer interface

    @Override
    public void readBook(Book book) { // method for output the message about reading book
        System.out.println(userName + " reading " + book.getTitle() + " by " + book.getAuthor()); // output the message
    }

    @Override
    public void listenBook(Book book) { // method for output the message about listening book
        System.out.println("No access"); // output because not all users can listen books
    };
}

class UserDecorator implements UserInterface, Observer { // create decorator to User class to extends it to another classes then
    User user;
    public UserDecorator(User user) {
        this.user = user;
    }
    @Override
    public void subscribe() {
        user.subscribe();
    } // call subscribe method of the User class

    @Override
    public void unsubscribe() {
        user.unsubscribe();
    } // call unsubscribe method of the User class

    @Override
    public void readBook(Book book) {
        user.readBook(book);
    } // call readBook method of the User class

    @Override
    public void listenBook(Book book) {
        System.out.println("No access");
    } // call listenBook method of the User class

    @Override
    public String getUserName() {
        return user.getUserName();
    } // rewrite getUserName method of the Observer interface
}

class StandardUser extends UserDecorator { // create child for UserDecorator class
    /*
    this class is no different from the User class, so I am only creating a constructor matching super()
     */
    StandardUser(User user) {
        super(user);
    }
}

class PremiumUser extends UserDecorator { // create child for UserDecorator class
    /*
    this class differs from the User class only by the listenBook method
    so, firstly, I will create a constructor matching super()
    then, I will override listenBook method
     */
    PremiumUser(User user) {
        super(user);
    } // constructor

    @Override
    public void listenBook(Book book) { // overriding listenBook method
        System.out.println(user.getUserName() + " listening " + book.getTitle() + " by " + book.getAuthor());
    }
}

interface UserFactory { // create a factory to creating instances of users
    UserDecorator createUser(String name); // method for creating instance of UserDecorator class
}

class StandardUserFactory implements UserFactory { // factory for creating instances of StandardUser class
    @Override
    public UserDecorator createUser(String name) {
        return new StandardUser(new User(name)); // override the method for creating instances
    }
}

class PremiumUserFactory implements UserFactory { // factory for creating instances of PremiumUser class
    @Override
    public UserDecorator createUser(String name) {
        return  new PremiumUser(new User(name)); // override the method for creating instances
    }
}
