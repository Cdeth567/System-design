/*
This program implements a hierarchy of abstract data structure classes in C++ for managing a collection of integers:
* DataStructure — defines a common interface for all structures (`insert`, `remove`, `search`).
* SequentialDataStructure — extends it with sequential operations (`pushBack`, `pushFront`, `popBack`, `popFront`).
* DynamicDataStructure — adds dynamic operations (`resize`, `clear`).
* List — a concrete class combining both sequential and dynamic behaviors using `std::vector<int>`.

The program processes commands like `insert x`, `remove`, and `search x`, outputs "YES" or "NO" for search queries, and finally prints the entire list.
*/

#include <iostream>
#include <vector>
#include <string>
// use namespace std to shorten code
using namespace std;

// create an abstract class DataStructure
class DataStructure
{
public:
    virtual void insert(int x) = 0;
    virtual void remove() = 0;
    virtual bool search(int x) = 0;
};

// create an abstract class SequentialDataStructure
class SequentialDataStructure : public DataStructure
{
public:
    virtual void pushBack(int x) = 0;
    virtual void pushFront(int x) = 0;
    virtual void popBack() = 0;
    virtual void popFront() = 0;
};

// create an abstract class DynamicDataStructure
class DynamicDataStructure : public DataStructure
{
public:
    virtual void resize(int new_size) = 0;
    virtual void clear() = 0;
};

// create a non-abstract class List
class List : public SequentialDataStructure, public DynamicDataStructure
{
private:
    vector<int> exampleList;
public:
    void insert(int x) override
    {
        exampleList.push_back(x);
    }

    void remove() override
    {
        // check if exampleList is not empty
        if (!exampleList.empty())
        {
            exampleList.pop_back();
        }
    }

    bool search(int x) override
    {
        // find element x in exampleList
        for (int el: exampleList)
        {
            if (el == x)
            {
                return true;
            }
        }
        return false;
    }

    void pushBack(int x) override
    {
        exampleList.push_back(x);
    }

    void pushFront(int x) override
    {
        exampleList.insert(exampleList.begin(), x);
    }

    void popBack() override
    {
        // check if exampleList is not empty
        if (!exampleList.empty())
        {
            exampleList.pop_back();
        }
    }

    void popFront() override
    {
        // check if exampleList is not empty
        if (!exampleList.empty())
        {
            exampleList.erase(exampleList.begin());
        }
    }

    void resize(int new_size) override
    {
        exampleList.resize(new_size);
    }

    void clear() override
    {
        exampleList.clear();
    }

    void printEl()
    {
        // print all elements from exampleList
        for (const auto& el : exampleList)
        {
            cout << el << " ";
        }
    }
};

int main()
{
    int n;
    cin >> n;
    // create an instance of the List class
    List listOfNumbers;
    for (int i = 0; i < n; i++) {
        string operation;
        cin >> operation;
        // perform operations
        if (operation == "remove")
        {
            listOfNumbers.remove();
        } else {
            int number;
            cin >> number;
            string el;
            if (operation == "insert") {
                listOfNumbers.insert(number);
            } else if (operation == "search")
            {
                // output YES if the item is found in the list
                if (listOfNumbers.search(number)) {
                    cout << "YES" << '\n';
                }
                // output NO if the item is not found in the list
                else {
                    cout << "NO" << '\n';
                }
            } else if (operation == "pushBack") {
                listOfNumbers.pushBack(number);
            } else if (operation == "pushFront") {
                listOfNumbers.pushFront(number);
            } else if (operation == "popBack") {
                listOfNumbers.popBack();
            } else if (operation == "popFront") {
                listOfNumbers.popFront();
            } else if (operation == "resize") {
                listOfNumbers.resize(number);
            } else if (operation == "clear") {
                listOfNumbers.clear();
            }
        }
    }
    // output all elements from the list
    listOfNumbers.printEl();
    return 0;
}
