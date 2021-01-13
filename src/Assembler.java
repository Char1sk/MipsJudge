import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Assembler {
    // Classes
    enum InstructionType {
        RType,
        IType,
        JType
    }
    class Instruction {
        protected InstructionType type;
        protected String content;
        protected String name;
    }
    class RTypeInstruction extends Instruction {
        protected String opcode;    //  6bit, 31:26
        protected String rs;        //  5bit, 25:21
        protected String rt;        //  5bit, 20:16
        protected String rd;        //  5bit, 15:11
        protected String shamt;     //  5bit, 10: 6
        protected String funct;     //  6bit,  5: 0
    }
    class ITypeInstruction extends Instruction {
        protected String opcode;    //  6bit, 31:26
        protected String rs;        //  5bit, 25:21
        protected String rt;        //  5bit, 20:16
        protected String offset;    // 16bit, 15: 0
    }
    class JTypeInstruction extends Instruction {
        protected String opcode;    //  6bit, 31:26
        protected String immediate; // 24bit, 25: 0
    }
    // Attributes
    // instructions related
    protected ArrayList<String> instructions;
    protected int totalNumber;
    protected int nowNumber;
    // static table related
    protected static HashMap<String, String> registerTable;
    protected static HashMap<String, InstructionType> typeTable;
    protected static HashMap<String, String> opcodeTable;
    protected static HashMap<String, String> functTable;
    // Functions
    // constructor
    public Assembler() {

    }
    // static table initialize
    protected static void initializeTables() {
        initializeRegisterTable();
        initializeTypeTable();
        initializeOpcodeTable();
        initializeFunctTable();
    }
    protected static void initializeRegisterTable() {
        registerTable = new HashMap<>();
        registerTable.put("$zero", "00000");
        registerTable.put("$at",   "00001");
        registerTable.put("$v0",   "00010");
        registerTable.put("$v1",   "00011");
        registerTable.put("$a0",   "00100");
        registerTable.put("$a1",   "00101");
        registerTable.put("$a2",   "00110");
        registerTable.put("$a3",   "00111");
        registerTable.put("$t0",   "01000");
        registerTable.put("$t1",   "01001");
        registerTable.put("$t2",   "01010");
        registerTable.put("$t3",   "01011");
        registerTable.put("$t4",   "01100");
        registerTable.put("$t5",   "01101");
        registerTable.put("$t6",   "01110");
        registerTable.put("$t7",   "01111");
        registerTable.put("$s0",   "10000");
        registerTable.put("$s1",   "10001");
        registerTable.put("$s2",   "10010");
        registerTable.put("$s3",   "10011");
        registerTable.put("$s4",   "10100");
        registerTable.put("$s5",   "10101");
        registerTable.put("$s6",   "10110");
        registerTable.put("$s7",   "10111");
        registerTable.put("$t8",   "11000");
        registerTable.put("$t9",   "11001");
        registerTable.put("$k0",   "11010");
        registerTable.put("$k1",   "11011");
        registerTable.put("$gp",   "11100");
        registerTable.put("$sp",   "11101");
        registerTable.put("$fp",   "11110");
        registerTable.put("$ra",   "11111");
    }
    protected static void initializeTypeTable() {
        typeTable = new HashMap<>();
        // load type
        typeTable.put("lb",      InstructionType.IType);
        typeTable.put("lbu",     InstructionType.IType);
        typeTable.put("lh",      InstructionType.IType);
        typeTable.put("lhu",     InstructionType.IType);
        typeTable.put("lw",      InstructionType.IType);
        // save type
        typeTable.put("sb",      InstructionType.IType);
        typeTable.put("sh",      InstructionType.IType);
        typeTable.put("sw",      InstructionType.IType);
        // R-R type
        typeTable.put("add",     InstructionType.RType);
        typeTable.put("addu",    InstructionType.RType);
        typeTable.put("sub",     InstructionType.RType);
        typeTable.put("subu",    InstructionType.RType);
        typeTable.put("mult",    InstructionType.RType);
        typeTable.put("multu",   InstructionType.RType);
        typeTable.put("div",     InstructionType.RType);
        typeTable.put("divu",    InstructionType.RType);
        typeTable.put("slt",     InstructionType.RType);
        typeTable.put("sltu",    InstructionType.RType);
        typeTable.put("sll",     InstructionType.RType);
        typeTable.put("srl",     InstructionType.RType);
        typeTable.put("sra",     InstructionType.RType);
        typeTable.put("sllv",    InstructionType.RType);
        typeTable.put("srlv",    InstructionType.RType);
        typeTable.put("srav",    InstructionType.RType);
        typeTable.put("and",     InstructionType.RType);
        typeTable.put("or",      InstructionType.RType);
        typeTable.put("xor",     InstructionType.RType);
        typeTable.put("nor",     InstructionType.RType);
        // R-I type
        typeTable.put("addi",    InstructionType.IType);
        typeTable.put("addiu",   InstructionType.IType);
        typeTable.put("andi",    InstructionType.IType);
        typeTable.put("ori",     InstructionType.IType);
        typeTable.put("xori",    InstructionType.IType);
        typeTable.put("lui",     InstructionType.IType);
        typeTable.put("slti",    InstructionType.IType);
        typeTable.put("sltiu",   InstructionType.IType);
        // branch type
        typeTable.put("beq",     InstructionType.IType);
        typeTable.put("bne",     InstructionType.IType);
        typeTable.put("blez",    InstructionType.IType);
        typeTable.put("bgtz",    InstructionType.IType);
        typeTable.put("bltz",    InstructionType.IType);
        typeTable.put("bgez",    InstructionType.IType);
        // jump type
        typeTable.put("j",       InstructionType.JType);
        typeTable.put("jal",     InstructionType.JType);
        typeTable.put("jalr",    InstructionType.RType);
        typeTable.put("jr",      InstructionType.RType);
        // move type
        typeTable.put("mfhi",    InstructionType.RType);
        typeTable.put("mflo",    InstructionType.RType);
        typeTable.put("mthi",    InstructionType.RType);
        typeTable.put("mtlo",    InstructionType.RType);
        // special type
        typeTable.put("eret",    InstructionType.RType);
        typeTable.put("mfc0",    InstructionType.RType);
        typeTable.put("mtc0",    InstructionType.RType);
        // trap type
        typeTable.put("break",   InstructionType.RType);
        typeTable.put("syscall", InstructionType.RType);
    }
    protected static void initializeOpcodeTable() {
        opcodeTable = new HashMap<>();
        opcodeTable.put("lb",      "100000");
        opcodeTable.put("lbu",     "100100");
        opcodeTable.put("lh",      "100001");
        opcodeTable.put("lhu",     "100101");
        opcodeTable.put("lw",      "100011");
        opcodeTable.put("sb",      "101000");
        opcodeTable.put("sh",      "101001");
        opcodeTable.put("sw",      "101011");
        opcodeTable.put("add",     "000000");
        opcodeTable.put("addu",    "000000");
        opcodeTable.put("sub",     "000000");
        opcodeTable.put("subu",    "000000");
        opcodeTable.put("mult",    "000000");
        opcodeTable.put("multu",   "000000");
        opcodeTable.put("div",     "000000");
        opcodeTable.put("divu",    "000000");
        opcodeTable.put("slt",     "000000");
        opcodeTable.put("sltu",    "000000");
        opcodeTable.put("sll",     "000000");
        opcodeTable.put("srl",     "000000");
        opcodeTable.put("sra",     "000000");
        opcodeTable.put("sllv",    "000000");
        opcodeTable.put("srlv",    "000000");
        opcodeTable.put("srav",    "000000");
        opcodeTable.put("and",     "000000");
        opcodeTable.put("or",      "000000");
        opcodeTable.put("xor",     "000000");
        opcodeTable.put("nor",     "000000");
        opcodeTable.put("addi",    "001000");
        opcodeTable.put("addiu",   "001001");
        opcodeTable.put("andi",    "001100");
        opcodeTable.put("ori",     "001101");
        opcodeTable.put("xori",    "001110");
        opcodeTable.put("lui",     "001111");
        opcodeTable.put("slti",    "001010");
        opcodeTable.put("sltiu",   "001011");
        opcodeTable.put("beq",     "000100");
        opcodeTable.put("bne",     "000101");
        opcodeTable.put("blez",    "000110");
        opcodeTable.put("bgtz",    "000111");
        opcodeTable.put("bltz",    "000001");
        opcodeTable.put("bgez",    "000001");
        opcodeTable.put("j",       "000010");
        opcodeTable.put("jal",     "000011");
        opcodeTable.put("jalr",    "000000");
        opcodeTable.put("jr",      "000000");
        opcodeTable.put("mfhi",    "000000");
        opcodeTable.put("mflo",    "000000");
        opcodeTable.put("mthi",    "000000");
        opcodeTable.put("eret",    "010000");
        opcodeTable.put("mfc0",    "010000");
        opcodeTable.put("mtc0",    "010000");
        opcodeTable.put("break",   "000000");
        opcodeTable.put("syscall", "000000");
    }
    protected static void initializeFunctTable() {
        functTable = new HashMap<>();
        functTable.put("add",     "100000");
        functTable.put("addu",    "100001");
        functTable.put("sub",     "100010");
        functTable.put("subu",    "100011");
        functTable.put("mult",    "011000");
        functTable.put("multu",   "011001");
        functTable.put("div",     "011010");
        functTable.put("divu",    "011011");
        functTable.put("slt",     "101010");
        functTable.put("sltu",    "101011");
        functTable.put("sll",     "000000");
        functTable.put("srl",     "000010");
        functTable.put("sra",     "000011");
        functTable.put("sllv",    "000100");
        functTable.put("srlv",    "000110");
        functTable.put("srav",    "000111");
        functTable.put("and",     "100100");
        functTable.put("or",      "100101");
        functTable.put("xor",     "100110");
        functTable.put("nor",     "100111");
        functTable.put("jalr",    "001001");
        functTable.put("jr",      "001000");
        functTable.put("mfhi",    "010000");
        functTable.put("mflo",    "010010");
        functTable.put("mthi",    "010001");
        functTable.put("mtlo",    "010011");
        functTable.put("eret",    "011000");
        functTable.put("break",   "001101");
        functTable.put("syscall", "001100");
    }
}

class TestClass {
    public static void main(String[] args) {
        try {
            Scanner inStream = new Scanner(new File("test_code.txt"));
        }
        catch (FileNotFoundException exception) {
            System.out.println("No such file!");
        }
        Assembler assembler = new Assembler();
    }
}