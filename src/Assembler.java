
/**
 * @author Char1sk
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Scanner;

/**
 * @author Char1sk
 */
public class Assembler {
    enum InstructionType {
        // Instruction Type
        Rtype,
        Itype,
        Jtype
    }
    class Instruction {
        // Attributes
        protected InstructionType type;
        protected String content;
        protected String name;
        // Constructors
        public Instruction() {

        }
        public Instruction(String s) {
            content = s.replace(',', ' ').toLowerCase(Locale.ROOT).trim();
            name = content.substring(0, content.indexOf(' '));
            type = typeTable.get(name);
            System.out.println(content);//
            System.out.println(name);//
            System.out.println(type);//
        }
    }
    class RtypeInstruction extends Instruction {
        protected String opcode;    //  6bit, 31:26
        protected String rs;        //  5bit, 25:21
        protected String rt;        //  5bit, 20:16
        protected String rd;        //  5bit, 15:11
        protected String shamt;     //  5bit, 10: 6
        protected String funct;     //  6bit,  5: 0
    }
    class ItypeInstruction extends Instruction {
        protected String opcode;    //  6bit, 31:26
        protected String rs;        //  5bit, 25:21
        protected String rt;        //  5bit, 20:16
        protected String offset;    // 16bit, 15: 0
    }
    class JtypeInstruction extends Instruction {
        protected String opcode;    //  6bit, 31:26
        protected String immediate; // 24bit, 25: 0
    }
    // Attributes

    protected ArrayList<Instruction> instructions;
    protected int totalNumber;
    protected int nowNumber;

    protected static HashMap<String, String> registerTable;
    protected static HashMap<String, InstructionType> typeTable;
    protected static HashMap<String, String> opcodeTable;
    protected static HashMap<String, String> functTable;

    public Assembler() {

    }
    public Assembler(String path) {
        initializeTables();
//                Instruction instruction = new Instruction(path);
//        instructions = new ArrayList<>();
//        try {
//            Scanner inStream = new Scanner(new File(path));
//        }
//        catch (FileNotFoundException exception) {
//            System.out.println("No such file");
//        }
    }

    protected static void initializeTables() {
        initializeRegisterTable();
        initializeTypeTable();
        initializeOpcodeTable();
        initializeFunctTable();
    }
    protected static void initializeRegisterTable() {
        registerTable = new HashMap<>(32);
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
        typeTable = new HashMap<>(64);
        // load type
        typeTable.put("lb",      InstructionType.Itype);
        typeTable.put("lbu",     InstructionType.Itype);
        typeTable.put("lh",      InstructionType.Itype);
        typeTable.put("lhu",     InstructionType.Itype);
        typeTable.put("lw",      InstructionType.Itype);
        // save type
        typeTable.put("sb",      InstructionType.Itype);
        typeTable.put("sh",      InstructionType.Itype);
        typeTable.put("sw",      InstructionType.Itype);
        // R-R type
        typeTable.put("add",     InstructionType.Rtype);
        typeTable.put("addu",    InstructionType.Rtype);
        typeTable.put("sub",     InstructionType.Rtype);
        typeTable.put("subu",    InstructionType.Rtype);
        typeTable.put("mult",    InstructionType.Rtype);
        typeTable.put("multu",   InstructionType.Rtype);
        typeTable.put("div",     InstructionType.Rtype);
        typeTable.put("divu",    InstructionType.Rtype);
        typeTable.put("slt",     InstructionType.Rtype);
        typeTable.put("sltu",    InstructionType.Rtype);
        typeTable.put("sll",     InstructionType.Rtype);
        typeTable.put("srl",     InstructionType.Rtype);
        typeTable.put("sra",     InstructionType.Rtype);
        typeTable.put("sllv",    InstructionType.Rtype);
        typeTable.put("srlv",    InstructionType.Rtype);
        typeTable.put("srav",    InstructionType.Rtype);
        typeTable.put("and",     InstructionType.Rtype);
        typeTable.put("or",      InstructionType.Rtype);
        typeTable.put("xor",     InstructionType.Rtype);
        typeTable.put("nor",     InstructionType.Rtype);
        // R-I type
        typeTable.put("addi",    InstructionType.Itype);
        typeTable.put("addiu",   InstructionType.Itype);
        typeTable.put("andi",    InstructionType.Itype);
        typeTable.put("ori",     InstructionType.Itype);
        typeTable.put("xori",    InstructionType.Itype);
        typeTable.put("lui",     InstructionType.Itype);
        typeTable.put("slti",    InstructionType.Itype);
        typeTable.put("sltiu",   InstructionType.Itype);
        // branch type
        typeTable.put("beq",     InstructionType.Itype);
        typeTable.put("bne",     InstructionType.Itype);
        typeTable.put("blez",    InstructionType.Itype);
        typeTable.put("bgtz",    InstructionType.Itype);
        typeTable.put("bltz",    InstructionType.Itype);
        typeTable.put("bgez",    InstructionType.Itype);
        // jump type
        typeTable.put("j",       InstructionType.Jtype);
        typeTable.put("jal",     InstructionType.Jtype);
        typeTable.put("jalr",    InstructionType.Rtype);
        typeTable.put("jr",      InstructionType.Rtype);
        // move type
        typeTable.put("mfhi",    InstructionType.Rtype);
        typeTable.put("mflo",    InstructionType.Rtype);
        typeTable.put("mthi",    InstructionType.Rtype);
        typeTable.put("mtlo",    InstructionType.Rtype);
        // special type
        typeTable.put("eret",    InstructionType.Rtype);
        typeTable.put("mfc0",    InstructionType.Rtype);
        typeTable.put("mtc0",    InstructionType.Rtype);
        // trap type
        typeTable.put("break",   InstructionType.Rtype);
        typeTable.put("syscall", InstructionType.Rtype);
    }
    protected static void initializeOpcodeTable() {
        opcodeTable = new HashMap<>(64);
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
        functTable = new HashMap<>(32);
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
//        Assembler assembler = new Assembler("./src/test_code.txt");
        Assembler assembler = new Assembler("    add    $t1,   $t2,   $t3  ");
    }
}