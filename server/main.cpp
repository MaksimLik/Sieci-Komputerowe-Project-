#include <iostream>
#include <thread>
#include <cstdio>
#include <string.h>
#include <vector>
#include "./server.h"
#include "./player.h"

Server server;
std::mutex searching_plaeyrs_mutex;

void newGame(Player p1, Player p2) {
    std::cout << "Odpalam gre" << std::endl;
    std::cout << "Gra" << p1.getName() << " " << p2.getName() << std::endl;
    
    server.send(p1.getFd(), (char*)"run", 3);
    server.send(p2.getFd(), (char*)"run", 3);

  // server.send(p1.getFd(), p2.getName().data(), strlen(p2.getName().data()));
 //  server.send(p2.getFd(), p1.getName().data(), strlen(p2.getName().data()));
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
                std::thread newGameThread(newGame, player1, player2);
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