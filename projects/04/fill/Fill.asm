// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Fill.asm

// Runs an infinite loop that listens to the keyboard input.
// When a key is pressed (any key), the program blackens the screen,
// i.e. writes "black" in every pixel;
// the screen should remain fully black as long as the key is pressed. 
// When no key is pressed, the program clears the screen, i.e. writes
// "white" in every pixel;
// the screen should remain fully clear as long as no key is pressed.

// Put your code here.

// We know that Screen: row-> 0~255 column -> 0~512 (pixel unit) or 0~32 (register unit)
// Screen memory map base address: 16384
// Keyboard memory map: 24576

// Store both screen and keyboard base addresses to certain variables
@SCREEN
D=A
@BASE_SCR_ADDR
M=D

@KBD
D=A
@BASE_KBD_ADDR
M=D

// 8192 = 255 x 32, which is the number of total units needed to draw
@8192
D=A
@TOTAL_UNIT
M=D

// Check if any key is pressed ?
//    if true then "draw black"
//    else go back to start and keep monitorting

(START)
@KBD
D=M;

@START
D;JEQ

// Draw black screen
// initialize i to 16384
@SCREEN
D=A
@i
M=D

(BLACK_LOOP)
@i
A=M
M=-1

@i
D=M
M=D+1

@BASE_SCR_ADDR
D=D-M
@TOTAL_UNIT
D=D-M

@BLACK_LOOP 
D;JLT

// Keep looping until all keys are unpressed
(PRESSED)
@KBD
D=M

@PRESSED
D;JNE

// Clean screen, almost the same as "draw black screen"
@SCREEN
D=A
@i
M=D

(WHITE_LOOP)
@i
A=M
M=0

@i
D=M
M=D+1

@BASE_SCR_ADDR
D=D-M
@TOTAL_UNIT
D=D-M

@WHITE_LOOP 
D;JLT

// Back to start
@START
0;JMP
