// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Mult.asm

// Multiplies R0 and R1 and stores the result in R2.
// (R0, R1, R2 refer to RAM[0], RAM[1], and RAM[2], respectively.)
//
// This program only needs to handle arguments that satisfy
// R0 >= 0, R1 >= 0, and R0*R1 < 32768.

// Put your code here.

// Initialize R2
@R2
M=0

// Check if R0 or R1 = 0 then go to the end of program
@R0
D=M

@END
D;JEQ

@R1
D=M

@END
D;JEQ

// Initialize i = 1
@i
M=1

// For i = 1 -> R1 do R2 = R2 + R0
(LOOP)
@R0
D=M

@R2
M=M+D

@i // inc i
M=M+1
D=M

@R1
D=D-M

@LOOP // i <= R1?
D;JLE

(END)
@END
0;JMP


