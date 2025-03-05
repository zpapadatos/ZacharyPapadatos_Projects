#include <iostream>
#include <string>
#include <cmath>
#include "hash.h"
using namespace std;

HashTable::HashTable(int k) { 
    size = k; //size of the number of buckets 
    table = new Node*[size](); //arrayt of pointers 
}

void HashTable::insert(string key) { 
    int index = hash_function(key); //index for hash
    Node* newNode = new Node(key); //crate node

    newNode->next = table[index]; //Insert
    table[index] = newNode; 
}

void HashTable::slotLengths() {
    for (int i = 0; i < size; i++) { //goes through all slots 
        int count = 0;
        Node* current = table[i]; 

        while (current != nullptr) { //counts the number of nodes in each slot
            count++; 
            current = current->next;
        }
        cout << "Slot " << i << ": " << count << endl; //prints count 
    }
}

void HashTable::printFirst5() { 
    int first5 = 0; 
    for (int i = 0; i < size && i < 5; i++) { 
        if (table[i] != nullptr) { //will do if slot is not empty 
            cout << "Slot " << i << ": "; 
            Node* current = table[i]; 
            while (current) { 
                cout << current->key << " "; //prints yhe element 
                current = current->next;
            }
            
            cout << endl;
            first5++; 
        }else { 
            cout << "Slot " << i << ": " << endl; //print the slot even if it is emoty 
        }
    }
}

double HashTable::standardDeviation() { 
    double totalElements = 0; 
    double numberofslots = size; 
    double slotLength[size]; //stores slot elements 
    double variance = 0; 
    
    for (int i = 0; i < size; i++) { //calc slot length 
        int count = 0; 
        Node* current = table[i]; 
        while (current) {
            count ++; //increase
            current = current->next; //set as next 
        }
        slotLength[i] = count; 
        totalElements += count; 
    }
    double mean = totalElements / numberofslots; //mean

    for (int i = 0; i < size; i ++) { //variance 
        double difference = slotLength[i] - mean;
        variance += (difference * difference); 
    }
    variance /= numberofslots; 

    double SD = sqrt(variance); //standard dev
    cout << SD << endl; 

    return sqrt(variance); 
}

int HashTable::hash_function(string text) {
    // Implement your own hash function here
   int hashValue = 0; 
   int primeNum[] = {2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97, 101, 103, 107, 109, 113, 127, 131, 137, 139, 149, 151, 157, 163, 167, 173, 179, 181, 191, 193, 197, 199,
    211, 223, 227, 229, 233, 239, 241, 251, 257, 263, 269, 271, 277, 281, 283, 293, 307, 311, 313, 317, 331, 337, 347, 349, 353, 359, 367, 373, 379, 383, 389, 397, 401, 409, 419, 421, 431, 433, 439, 443, 449, 457,
    461, 463, 467, 479, 487, 491, 499};
   int closest = primeNum[0]; 
   int mindifference = abs(size - primeNum[0]); 

   for (int prime : primeNum) { //will pick the prime closests to the size 
    int difference = abs(size - prime); 
    if (difference < mindifference) { 
        mindifference = difference;
        closest = prime; 
    }
    }

   for (char c : text) { 
    hashValue = (hashValue * closest + c) % size;  

   } 
   return hashValue;

}
   
