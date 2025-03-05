#ifndef HASH_H
#define HASH_H

#include <iostream>
#include <string>
#include <cmath>
// You are free to use additional libraries as long as it's not PROHIBITED per instruction.
using namespace std;

struct Node{ 
    string key; 
    Node* next; 

    Node(string k) { 
        key = k; 
        next = nullptr; 
    }
};

class HashTable { 
    private: 
    int size; 
    Node** table; 

    public: 
    HashTable(int k); //construct 
    
    int hash_function(string text); 
    void insert(string key); 
    void printFirst5(); 
    void slotLengths(); 
    double standardDeviation(); 
};

#endif 
