.data
newline:	.asciiz "\n" 
.text
.globl main
main:
li $fp, 0x7ffffffc 

Block1:

# loadI r_SMALLER , 0  
li $t0 , 0  
sw $t0 , 0($fp)  

# loadI r_BIGGER , 0  
li $t0 , 0  
sw $t0 , -4($fp)  

# loadI r_TEMP , 0  
li $t0 , 0  
sw $t0 , -8($fp)  

# readInt r_BIGGER  
li $v0 , 5  
syscall
add $t0 , $v0 , $zero 
sw $t0 , -4($fp)  

# readInt r_SMALLER  
li $v0 , 5  
syscall
add $t0 , $v0 , $zero 
sw $t0 , 0($fp)  

# cmp_GT r_0 , r_SMALLER , r_BIGGER 
lw $t0 , 0($fp)  
lw $t1 , -4($fp)  
sgt $t0 , $t0 , $t1 
sw $t0 , -12($fp)  

# cbr r_0, Block2, Block4  
lw $t2 , -12($fp)  
bne $t2 , $zero , Block2 
j Block4  

Block2:

# i2i r_TEMP , r_SMALLER  
lw $t0 , 0($fp)  
add $t0 , $t0 , $zero 
sw $t0 , -8($fp)  

# i2i r_SMALLER , r_BIGGER  
lw $t1 , -4($fp)  
add $t1 , $t1 , $zero 
sw $t1 , 0($fp)  

# i2i r_BIGGER , r_TEMP  
lw $t2 , -8($fp)  
add $t2 , $t2 , $zero 
sw $t2 , -4($fp)  

Block4:

# jumpl Block5  
j Block5  

Block5:

# loadI r_1 , 0  
li $t3 , 0  
sw $t3 , -16($fp)  

# cmp_GT r_2 , r_SMALLER , r_1 
lw $t0 , 0($fp)  
lw $t1 , -16($fp)  
sgt $t0 , $t0 , $t1 
sw $t0 , -20($fp)  

# cbr r_2, Block6, Block7  
lw $t2 , -20($fp)  
bne $t2 , $zero , Block6 
j Block7  

Block6:

# sub r_3 , r_BIGGER , r_SMALLER 
lw $t0 , -4($fp)  
lw $t1 , 0($fp)  
subu $t0 , $t0 , $t1 
sw $t0 , -24($fp)  

# i2i r_BIGGER , r_3  
lw $t0 , -24($fp)  
add $t0 , $t0 , $zero 
sw $t0 , -4($fp)  

# cmp_GT r_4 , r_SMALLER , r_BIGGER 
lw $t1 , 0($fp)  
lw $t2 , -4($fp)  
sgt $t1 , $t1 , $t2 
sw $t1 , -28($fp)  

# cbr r_4, Block8, Block10  
lw $t3 , -28($fp)  
bne $t3 , $zero , Block8 
j Block10  

Block8:

# i2i r_TEMP , r_SMALLER  
lw $t0 , 0($fp)  
add $t0 , $t0 , $zero 
sw $t0 , -8($fp)  

# i2i r_SMALLER , r_BIGGER  
lw $t1 , -4($fp)  
add $t1 , $t1 , $zero 
sw $t1 , 0($fp)  

# i2i r_BIGGER , r_TEMP  
lw $t2 , -8($fp)  
add $t2 , $t2 , $zero 
sw $t2 , -4($fp)  

Block10:

# jumpl Block5  
j Block5  

Block7:

# writeInt r_BIGGER   
li $v0 , 1  
lw $t3 , -4($fp)  
add $a0 , $t3 , $zero 
syscall
li $v0 , 4  
la $a0 , newline  
syscall

# exit 
li $v0, 10 
syscall 

