#ifndef __SERVER_H__	
#define __SERVER_H__

#include <iostream>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <vector>
#include <player.h>

class Server
{
	private:
		int sockfd;
		unsigned int portno;
		unsigned int players;
		
	
	public:
		Server();
		~Server();
		int getsockfd();
		void error(char * msg);
		void launch();
		int connection();
		void receive(int socket, char* buffer);
		void send(int socket, char* buffer, int size);
		void goodbye();
		std::vector<Player> waitingPlayers; 
		std::vector<Player> playersInGame;

};

#endif