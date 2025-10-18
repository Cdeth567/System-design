/*
This program defines a Complex class to perform arithmetic and comparison operations on complex numbers.
It supports: Addition, Subtraction, Multiplication, Division, Equality check

Each complex number is represented as `a+bi`, where `a` and `b` are real numbers with 2 decimal places.
Input specifies multiple operations on pairs of complex numbers, and the output prints either:
the resulting complex number, or true / false for equality.
*/

#include <iostream>
#include <iomanip>
#include <cmath>
// Use namespace std to shorten code
using namespace std;

// Create class Complex
class Complex
{
// Create variables for parts of complex number
private:
    double first;
    double second;
// Describe methods for class
public:
    Complex(double first = 0.0, double second = 0.0) : first(first), second(second) {}
    // Addition
    Complex operator+(const Complex& complex) const
    {
        return Complex(first + complex.first, second + complex.second);
    }
    // Subtraction
    Complex operator-(const Complex& complex) const
    {
        return Complex(first - complex.first, second - complex.second);
    }
    // Multiplication
    Complex operator*(const Complex& complex) const
    {
        return Complex(first * complex.first - second * complex.second, first * complex.second + second * complex.first);
    }
    // Division
    Complex operator/(const Complex& complex) const
    {
        double d = pow(complex.first, 2) + pow(complex.second, 2);
        return Complex((first * complex.first + second * complex.second) / d, (second * complex.first - first * complex.second) / d);
    }
    // Equality
    bool operator==(const Complex& complex) const
    {
        return (first == complex.first) && (second == complex.second);
    }
    // Method for output complex numbers
    friend ostream& operator<<(ostream& s, const Complex& complex)
    {
        s << fixed << setprecision(2) << complex.first;
        if (complex.second >= 0.0)
        {
            s << "+";
        }
        s << fixed << setprecision(2) << complex.second << "i";
        return s;
    }
};

int main()
{
    int n;
    cin >> n;
    char op;
    double a, b, c, d;
    for (int i = 0; i < n; i++)
    {
        // read numbers and signs of operations
        cin >> op >> a >> b >> c >> d;
        Complex c1(a, b);
        Complex c2(c, d);
        // Perform operations
        switch (op)
        {
            case '+':
                cout << (c1 + c2) << '\n';
                break;
            case '-':
                cout << (c1 - c2) << '\n';
                break;
            case '*':
                cout << (c1 * c2) << '\n';
                break;
            case '/':
                cout << (c1 / c2) << '\n';
                break;
            case '=':
                cout << boolalpha << (c1 == c2) << '\n';
                break;
            default:
                break;
        }
    }
    return 0;
}
