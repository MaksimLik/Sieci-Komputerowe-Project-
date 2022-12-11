#include <iostream>
#include <thread>
#include <cstdio>
#include <string.h>
#include <vector>
#include "./server.h"
#include "./player.h"

Server server;
std::mutex searching_plaeyrs_mutex;

void newGame(Player* p1, Player* p2) {
    std::cout << "Odpalam gre" << std::endl;
    std::cout << "Gra" << p1->getName() << " " << p2->getName() << std::endl;
    
    printf("Gracz 1 %d\n", p1->getFd());
    printf("Gracz 2 %d\n", p2->getFd());

    server.send(p1->getFd(), (char*)"run", 3);
    server.send(p2->getFd(), (char*)"run", 3);

    printf("Waiting for ready ");

    char buf[100];
    server.receive(p1->getFd(), buf);
    printf("%d %s", (int)strlen(buf), buf);

    // while (!p1->readyToPlay )
    // {
    //     char buf[100];
    //     int sizeMess = server.receiveBlock(p1->getFd(),buf, 100);
    //     printf("%s %d", buf, strlen(buf));
    //     if (sizeMess < 100) {
    //         // send request to send again board //  
    //         server.send(p1->getFd(), (char*)"repeat", 5);
    //     } else {
    //         p1->readyToPlay = true;
    //     }
    // }
    
    //  while (!p2->readyToPlay )
    // {
    //     char buf[100];
    //     int sizeMess = server.receiveBlock(p2->getFd(),buf, 100);
    //     if (sizeMess < 100) {
    //         // send request to send again board //  
    //         server.send(p2->getFd(), (char*)"repeat", 5);
    //     } else {
    //         p2->readyToPlay = true;
    //     }
    // }
    
    if (p1->readyToPlay && p2->readyToPlay) {
        server.send(p1->getFd(), (char*)"ready", 5);
        server.send(p2->getFd(), (char*)"ready", 5);
    }
 
}


void searchPlayers() {
    while (true) {        
        searching_plaeyrs_mutex.lock();

            // creating new player
            int fd = server.connection();
            char buf[100];
            server.receive(fd, buf);
            printf("%s\n", buf);
            std::string playerName = std::string(buf); 

            Player p(fd, playerName);
            server.waitingPlayers.push_back(p);

            if (server.waitingPlayers.size() == 2) {     
                // run new game
                Player player1 = server.waitingPlayers[0];
                Player player2 = server.waitingPlayers[1];
                server.waitingPlayers.clear();
                std::thread newGameThread(newGame, &player1, &player2);
                newGameThread.detach();
            }
       searching_plaeyrs_mutex.unlock();
    }
}

int main(int argc, char** argv) {

    server.launch();
    std::thread searchingPlayersThread(searchPlayers); 
    searchingPlayersThread.join();

    return 0;
}