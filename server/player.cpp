#include "./player.h"
#include <string>


Player::Player(int fd, std::string name) {
    this->fd = fd;
    this->name = name;

}

std::string Player::getName() {
    return this->name;
}


int Player::getFd() {
    return this->fd;
}