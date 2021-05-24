//
// Created by liwu shu on 2020/12/9.
//

typedef union epoll_data
{
    void *ptr;
    int fd;
    int u32;
    int u64;
} epoll_data_t;

struct epoll_event
{
    int events;	/* Epoll events */
    epoll_data_t data;	/* User data variable */
};
