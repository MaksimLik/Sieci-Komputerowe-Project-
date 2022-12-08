#include "./server.h"

	Server::Server()
	{
		this->sockfd = 0;
		this->portno = 0;
		this->players = 0;
	
	}

	Server::~Server()
	{
		close(this->sockfd);
	}

	int Server::getsockfd()
	{
		return this->sockfd;
	}

	void Server::error(char * msg)
	{
		perror(msg);
		exit(1);
	}

	void Server::launch()
	{
		int port = -1;
		struct sockaddr_in serv_addr;

		while(port < 0 || port > 65535)
		{
			std::cout << std::endl << "Wpisz numer portu (typ <<unsigned int>>)" << std::endl;
			std::cin >> port;
		}

		this->sockfd = socket(AF_INET, SOCK_STREAM, 0);
		if (this->sockfd < 0)
			error((char *) "ERROR opening socket");


		bzero((char *) &serv_addr, sizeof(serv_addr));
		this->portno = port;
		serv_addr.sin_family = AF_INET;
		serv_addr.sin_port = htons(this->portno);
		serv_addr.sin_addr.s_addr = INADDR_ANY;
		std::cout << "Listening" << std::endl;
		if (bind(this->sockfd, (struct sockaddr *) &serv_addr, sizeof(serv_addr)) < 0)
			error((char *) "ERROR binding");

		listen(this->sockfd, 2);
	}

	int Server::connection()
	{
		int newsockfd;
		struct sockaddr_in cli_addr;
		unsigned int clilen = sizeof(cli_addr);
		newsockfd = accept(this->sockfd, (struct sockaddr *) &cli_addr, &clilen);
		std::cout << "Client fd : " << newsockfd <<  " accepted" << std::endl;
		if (newsockfd < 0)
			error((char *) "ERROR on accept");
		this->players++;

		return newsockfd;
	}

	void Server::receive(int socket, char* buffer)
	{
		
		bzero(buffer, 100);
		printf("Read ");
		int n = read(socket, buffer, 100);	
		if (n < 0)
			error((char *) "ERROR reading from socket");
		printf("Wiadomosc od klienta: %s", buffer);
	}

	void Server::send(int socket, char* buffer, int size)
	{
		int n;
		//std::cout << "Wielkosc bufora: " <<  size  << std::endl;
		n = write(socket, buffer, size);
		if (n < 0)
			error((char *) "ERROR writing to socket");
		
	}



	void Server::goodbye()
	{
		this->players--;
	}
	