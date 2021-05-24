cmake_minimum_required(VERSION 3.4.1)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++14")

add_compile_options(
        -Weverything
#        -Werror
        -Wunused-variable
        -Wunneeded-internal-declaration
        -Wno-c++98-compat-pedantic
        -Wno-old-style-cast)

add_library(loop_hook SHARED
        loop_hook.cpp
        )

target_include_directories(loop_hook PUBLIC
        ${BITS_HEADER_DIR}
        )

target_link_libraries(loop_hook
        ${BITS_SO_DIR}/${CMAKE_ANDROID_ARCH_ABI}/libbytehook.so
        log)
