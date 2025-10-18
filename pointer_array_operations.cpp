/*
This program manipulates an integer array using pointers and references only, without direct indexing. It supports two operations:

1. set - updates the value of the i-th element to j.
2. sum - computes the sum of elements between indices i and j (inclusive).

Input includes the array size, operations count, array elements, and a list of operations.
For each 'sum' operation, the result is printed immediately.
At the end, the program outputs the final state of the modified array.
*/

#include <iostream>
#include <vector>
// use namespace to shorten the code
using namespace std;

// creating a function to calculate the sum
int sum(int initialIndex, int finalIndex, const vector<int>& seq)
{
    int sum = 0;
    for (int i = initialIndex; i <= finalIndex; i++)
        // adding values to the sum variable, initially equal to zero, to calculate the sum
        sum += seq[i];
    return sum;
}

int main()
{
    int n, m;
    cin >> n >> m;
    vector<int> sequence(n);
    for (int i = 0; i < n; i++)
        // reading the second line of input into the array
        cin >> sequence[i];
    for (int s = 0; s < m; s++)
    {
        string operation;
        cin >> operation;
        int i, j;
        cin >> i >> j;
        // performing the set operation
        if (operation == "set") sequence[i] = j;
        // performing the sum operation
        else if (operation == "sum") cout << sum(i, j, sequence) << '\n';
    }
    // output of all modified array elements
    for (int ind = 0; ind < n; ind++)
        cout << sequence[ind] << " ";
    return 0;
}
