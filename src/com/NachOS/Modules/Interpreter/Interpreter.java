package com.NachOS.Modules.Interpreter;

import com.NachOS.Modules.Interpreter.IInterpreter;
import com.NachOS.Modules.ProcessManager.IProcessManager;
import com.NachOS.Modules.FileSystem.IFileSystem;
import com.NachOS.Modules.Communication.IPC;
import com.NachOS.Modules.Communication.Sms;
import com.NachOS.Modules.ACL.Interfaces.ILoginService;
import com.NachOS.Modules.ProcessManager.PCB;

import java.util.ArrayList;

public class Interpreter implements IInterpreter {

    /**
     Rozkazy:
        Arytmetyczno-logiczne
        AD reg1 reg2 - dodaje rejestr2 do rejestru1,
        AX reg num – dodaje liczbę do rejestru,
        SB reg1 reg2 - odejmuje od rejestru1 zawartość rejestru2,
        SX reg num – odejmuje liczbę od rejestru,
        DC reg - zmniejsza zawartość rejestru o 1,
        IC reg - zwiększa zawartość rejestru o 1,
        MU reg1 reg2 – mnoży rejestr 1 przez rejestr 2,
        MX reg num – mnoży rejestr przez liczbę,
        DV reg1 reg2 - dzieli zawartość rejestru1 przez zawartość rejestru2,
        DX reg num – dzieli rejestr przez liczbę,
        MV reg1 reg2 – kopiuje zawartość rejestru 2 do rejestru 1,
        MZ address reg - zapisuje do pamięci zawartość rejestru pod wskazanym adresem,
        MO reg n – umieszcza w rejestrze wartość n,
        MY reg address - umieszcza w rejestrze zawartość pamiętaną pod wskazanym adresem,
        JP counter - skacze do innego rozkazu poprzez zmianę licznika,
        JZ reg n - skok przy zerowej zawartości rejestru będącego argumentem,
        PE reg - wyświetla wynik programu znajdujący się w podanym rejestrze,

        Procesy
        KP nazwa - usunięcie procesu o danej nazwie,
        RP nazwa – uruchamia proces o danej nazwie,

        Pliki
        CE file_name - tworzy pusty plik o podanej nazwie,
        OF file_name - otwiera plik o podanej nazwie
        CL file_name - zamyka plik o podanej nazwie,
        CF file_name file_content - tworzy plik z zawartością,
        AF file_name file_content - dodaje dane na końcu pliku,
        DF file_name - usuwa plik o danej nazwie,
        RF file_name - czyta plik o podanej nazwie,
        RN old_file_name new_file_name - zmienia nazwę pliku

        Komunikaty
        RM  - zapisywanie otrzymanego komunikatu do RAM,
        SM  - wysłanie komunikatu, 

        EX - kończy program
     **/

    private int A = 0;
    private int B = 0;
    private int C = 0;
    private int PC = 0;

    private IProcessManager processManager;
    private IFileSystem fileSystem;
    private IPC communication;
    private ILoginService loginService;

    public Interpreter(IProcessManager processManager,
                       IFileSystem fileSystem,
                       IPC communication,
                       ILoginService loginService) {
        this.processManager = processManager;
        this.fileSystem = fileSystem;
        this.communication = communication;
        this.loginService = loginService;
    }

    private void DownloadRegisters() {
        A = processManager.getActivePCB().getRegister(PCB.Register.A);
        B = processManager.getActivePCB().getRegister(PCB.Register.B);
        C = processManager.getActivePCB().getRegister(PCB.Register.A);
        PC = processManager.getActivePCB().getCounter();
    }

    private void RegisterStatus() {
        System.out.println("Register A: " + A);
        System.out.println("Register B: " + B);
        System.out.println("Register C: " + C);
        System.out.println("Register PC: " + PC);
        System.out.println();
    }

    private void SaveRegister() {
        processManager.getActivePCB().setRegister(PCB.Register.A, A);
        processManager.getActivePCB().setRegister(PCB.Register.B, B);
        processManager.getActivePCB().setRegister(PCB.Register.C, C);
        processManager.getActivePCB().setCounter(PC);
    }

    //------------------------ARYTMETYCZNO-LOGICZNE----------------------------------

    //AD reg1 reg2 - dodaje rejestr2 do rejestru1
    private void AD(String[] order) {
        String reg_1 = order[1];
        String reg_2 = order[2];

        switch (reg_1) {
            case "A":
                switch (reg_2) {
                    case "B":
                        A += B;
                        break;
                    case "C":
                        A += C;
                        break;
                    default:
                        System.out.println("Incorrect register.");
                        break;
                }
                break;
            case "B":
                switch (reg_2) {
                    case "A":
                        B += A;
                        break;
                    case "C":
                        B += C;
                        break;
                    default:
                        System.out.println("Incorrect register.");
                        break;
                }
                break;
            case "C":
                switch (reg_2) {
                    case "A":
                        C += A;
                        break;
                    case "B":
                        C += B;
                        break;
                    default:
                        System.out.println("Incorrect register.");
                        break;
                }
                break;
            default:
                System.out.println("Incorrect register.");
                break;
        }
    }

    //AX reg num – dodaje liczbę do rejestru
    private void AX(String[] order) {
        String reg = order[1];
        int val = Integer.parseInt(order[2]);

        switch (reg) {
            case "A":
                A += val;
                break;
            case "B":
                B += val;
                break;
            case "C":
                C += val;
                break;
            default:
                System.out.println("Incorrect register.");
                break;
        }
    }

    //SB reg1 reg2 - odejmuje od rejestru1 zawartość rejestru2
    private void SB(String[] order) {
        String reg_1 = order[1];
        String reg_2 = order[2];

        switch (reg_1) {
            case "A":
                switch (reg_2) {
                    case "B":
                        A -= B;
                        break;
                    case "C":
                        A -= C;
                        break;
                    default:
                        System.out.println("Incorrect register.");
                        break;
                }
                break;
            case "B":
                switch (reg_2) {
                    case "A":
                        B -= A;
                        break;
                    case "C":
                        B -= C;
                        break;
                    default:
                        System.out.println("Incorrect register.");
                        break;
                }
                break;
            case "C":
                switch (reg_2) {
                    case "A":
                        C -= A;
                        break;
                    case "B":
                        C -= B;
                        break;
                    default:
                        System.out.println("Incorrect register.");
                        break;
                }
                break;
            default:
                System.out.println("Incorrect register.");
                break;
        }
    }

    //SX reg num – odejmuje liczbę od rejestru
    private void SX(String[] order) {
        String reg = order[1];
        int val = Integer.parseInt(order[2]);

        switch (reg) {
            case "A":
                A -= val;
                break;
            case "B":
                B -= val;
                break;
            case "C":
                C -= val;
                break;
            default:
                System.out.println("Incorrect register.");
                break;
        }
    }

    //DC reg - zmniejsza zawartość rejestru o 1
    private void DC(String[] order) {
        String reg = order[1];

        switch (reg) {
            case "A":
                A -= 1;
                break;
            case "B":
                B -= 1;
                break;
            case "C":
                C -= 1;
                break;
            default:
                System.out.println("Incorrect register.");
                break;
        }
    }

    //IC reg - zwiększa zawartość rejestru o 1
    private void IC(String[] order) {
        String reg = order[1];

        switch (reg) {
            case "A":
                A += 1;
                break;
            case "B":
                B += 1;
                break;
            case "C":
                C += 1;
                break;
            default:
                System.out.println("Incorrect register.");
                break;
        }
    }

    //MU reg1 reg2 – mnoży rejestr 1 przez rejestr 2
    private void MU(String[] order) {
        String reg_1 = order[1];
        String reg_2 = order[2];

        switch (reg_1) {
            case "A":
                switch (reg_2) {
                    case "B":
                        A *= B;
                        break;
                    case "C":
                        A *= C;
                        break;
                    default:
                        System.out.println("Incorrect register.");
                        break;
                }
                break;
            case "B":
                switch (reg_2) {
                    case "A":
                        B *= A;
                        break;
                    case "C":
                        B *= C;
                        break;
                    default:
                        System.out.println("Incorrect register.");
                        break;
                }
                break;
            case "C":
                switch (reg_2) {
                    case "A":
                        C *= A;
                        break;
                    case "B":
                        C *= B;
                        break;
                    default:
                        System.out.println("Incorrect register.");
                        break;
                }
                break;
            default:
                System.out.println("Incorrect register.");
                break;
        }
    }

    //MX reg num – mnoży rejestr przez liczbę
    private void MX(String[] order) {
        String reg = order[1];
        int val = Integer.parseInt(order[2]);

        switch (reg) {
            case "A":
                A *= val;
                break;
            case "B":
                B *= val;
                break;
            case "C":
                C *= val;
                break;
            default:
                System.out.println("Incorrect register.");
                break;
        }
    }

    //DV reg1 reg2 - dzieli zawartość rejestru1 przez zawartość rejestru2
    //ogarnąć gdy rowne 0
    private void DV(String[] order) {
        String reg_1 = order[1];
        String reg_2 = order[2];

        switch (reg_1) {
            case "A":
                switch (reg_2) {
                    case "B":
                        A /= B;
                        break;
                    case "C":
                        A /= C;
                        break;
                    default:
                        System.out.println("Incorrect register.");
                        break;
                }
                break;
            case "B":
                switch (reg_2) {
                    case "A":
                        B /= A;
                        break;
                    case "C":
                        B /= C;
                        break;
                    default:
                        System.out.println("Incorrect register.");
                        break;
                }
                break;
            case "C":
                switch (reg_2) {
                    case "A":
                        C /= A;
                        break;
                    case "B":
                        C /= B;
                        break;
                    default:
                        System.out.println("Incorrect register.");
                        break;
                }
                break;
            default:
                System.out.println("Incorrect register.");
                break;
        }
    }

    //DX reg num – dzieli rejestr przez liczbę
    private void DX(String[] order) {
        String reg = order[1];
        int val = Integer.parseInt(order[2]);

        if (val != 0) {
            switch (reg) {
                case "A":
                    A /= val;
                    break;
                case "B":
                    B /= val;
                    break;
                case "C":
                    C /= val;
                    break;
                default:
                    System.out.println("Incorrect register.");
                    break;
            }
        }else{
            System.out.println("Division by zero");
        }
    }

    //MV reg1 reg2 – kopiuje zawartość rejestru 2 do rejestru 1
    private void MV(String[] order) {
        String reg_1 = order[1];
        String reg_2 = order[2];

        switch (reg_1) {
            case "A":
                switch (reg_2) {
                    case "B":
                        A = B;
                        break;
                    case "C":
                        A = C;
                        break;
                    default:
                        System.out.println("Incorrect register.");
                        break;
                }
                break;
            case "B":
                switch (reg_2) {
                    case "A":
                        B = A;
                        break;
                    case "C":
                        B = C;
                        break;
                    default:
                        System.out.println("Incorrect register.");
                        break;
                }
                break;
            case "C":
                switch (reg_2) {
                    case "A":
                        C = A;
                        break;
                    case "B":
                        C = B;
                        break;
                    default:
                        System.out.println("Incorrect register.");
                        break;
                }
                break;
            default:
                System.out.println("Incorrect register.");
                break;
        }
    }

    //MZ address reg - zapisuje do pamięci zawartość rejestru pod wskazanym adresem,
    //Klaudia metoda do zapisywania do pamięci
    private void MZ(String[] order) {
        String raw_address = order[1];
        String register = order[2];
        String[] split_address = raw_address.split("");

        if ((!split_address[0].equals("[")) || (!split_address[raw_address.length() - 1].equals("]"))) {
            System.out.println("Incorrect address.");
        } else {
            raw_address = raw_address.replaceAll("\\[", "").replaceAll("]", "");
            int address = Integer.parseInt(raw_address);
            if (register.equals("A")) {
                //memory.writeMemory((char) A, address);
            } else if (register.equals("B")) {
                //memory.writeMemory((char) B, address);
            } else if (register.equals("C")) {
                //memory.writeMemory((char) C, address);
            } else {
                System.out.println("Incorrect register.");
            }
        }
    }

    //MO reg n – umieszcza w rejestrze wartość n,
    private void MO(String[] order) {
        String reg = order[1];
        int val = Integer.parseInt(order[2]);

        switch (reg) {
            case "A":
                A = val;
                break;
            case "B":
                B = val;
                break;
            case "C":
                C = val;
                break;
            default:
                System.out.println("Incorrect register.");
                break;
        }
    }

    //MY reg address - umieszcza w rejestrze zawartość pamiętaną pod wskazanym adresem,
    //Klaudia metoda do zapisywania do pamięci
    private void MY(String[] order) {
        String register = order[1];
        String raw_address = order[2];
        String[] split_address = raw_address.split("");

        if ((!split_address[0].equals("[")) || (!split_address[raw_address.length() - 1].equals("]"))) {
            System.out.println("Incorrect address.");
        } else {
            raw_address = raw_address.replaceAll("\\[", "").replaceAll("]", "");
            int address = Integer.parseInt(raw_address);
           /*char pom = memory.readMemory(address);

            if (pom != '#') {
                if (register.equals("A")) {
                    A = memory.readMemory(address);
                } else if (register.equals("B")) {
                    B = memory.readMemory(address);
                } else if (register.equals("C")) {
                    C = memory.readMemory(address);
                } else {
                    System.out.println("Incorrect register.");
                }
            } else {
                System.out.println("Address is empty.");
            }*/
        }
    }

    //JP counter - skacze do innego rozkazu poprzez zmianę licznika
    private void JP(String[] order) {
        int counter = Integer.parseInt(order[1]);
        PC = counter - 1;
    }

    //JZ reg n - skok przy zerowej zawartości rejestru będącego argumentem,
    private void JZ(String[] order) {
            String reg = order[1];
            int counter = Integer.parseInt(order[2]);

            switch (reg){
                case "A":
                    if (A == 0) {
                        PC = counter - 1;
                    }
                    break;
                case "B":
                    if (B == 0) {
                        PC = counter - 1;
                    }
                    break;
                case "C":
                    if (C == 0) {
                        PC = counter - 1;
                    }
                    break;
                default:
                    System.out.println("Incorrect register");
                    break;
            }
    }

    //PE reg - wyświetla wynik programu znajdujący się w podanym rejestrze,
    private void PE(String[] order) {
        String reg = order[1];
        switch (reg) {
            case "A":
                    System.out.println("Result: " + A);
                    break;
            case "B":
                    System.out.println("Result: " + B);
                    break;
            case "C":
                    System.out.println("Result: " + C);
                    break;
            default:
                    System.out.println("Incorrect register.");
                    break;
                }
        }

    //---------------------------------PROCESY---------------------------------------

    //KP nazwa - usunięcie procesu o danej nazwie
    //ogarnąć
    private void DP(String[] order){

    }

    //RP nazwa – uruchamia proces o danej nazwie
    //ogarnąć
    private void RP(String[] order){

    }

    //-----------------------------------PLIKI---------------------------------------

    //CE file_name - tworzy pusty plik o podanej nazwie
    private void CE(String[] order) throws Exception {
        try {
            String filename = order[1];
            fileSystem.createFile(filename, loginService.getLoggedUser(), processManager);
        } catch (Exception e) {
            throw e;
        }
    }

    //OF file_name - otwiera plik o podanej nazwie
    private void OF(String[] order) throws Exception {
        try {
            String filename = order[1];
            fileSystem.openFile(filename);
        } catch (Exception e) {
            throw e;
        }
    }

    //CL file_name - zamyka plik o podanej nazwie
    private void CL(String[] order) throws Exception {
        try {
            String filename = order[1];
            fileSystem.closeFile(filename);
        } catch (Exception e) {
            throw e;
        }
    }

    //CF file_name file_content - tworzy plik z zawartością
    //ogarnąć żeby mi się zgadzało ze splitem
    private void CF(String[] order) throws Exception {
        try {
            String filename = order[1];
            int n = order.length;

            String fileContent;
            for (int i = 2; i < n; i++) {
                //fileContent += order[i];
            }
            fileSystem.createFile(filename, loginService.getLoggedUser(), processManager);
            fileSystem.openFile(filename);
            //fileSystem.appendFile(filename, fileContent);
            fileSystem.closeFile(filename);
        } catch (Exception e) {
            throw e;
        }
    }

    //AF file_name file_content - dodaje dane na końcu pliku
    private void AF(String[] order) throws Exception {
        try {
            String filename = order[1];
            String fileContent = order[2];
            fileSystem.appendFile(filename, fileContent);
        } catch (Exception e) {
            throw e;
        }
    }

    //DF file_name - usuwa plik o danej nazwie
    private void DF(String[] order) throws Exception {
        try {
            String filename = order[1];
            fileSystem.deleteFile(filename);
        } catch (Exception e) {
            throw e;
        }
    }

    //RF file_name - czyta plik o podanej nazwie
    private void RF(String[] order) throws Exception {
        try {
            String filename = order[1];
            String fileContent = fileSystem.readFile(filename);
            System.out.println("File content:");
            System.out.println(fileContent);
        } catch (Exception e) {
            throw e;
        }
    }

    //RN old_file_name new_file_name - zmienia nazwę pliku
    private void RN(String[] order) throws Exception {
        try {
            String oldName = order[1];
            String newName = order[2];
            fileSystem.renameFile(oldName, newName);
        } catch (Exception e) {
            throw e;
        }
    }

    //-----------------------------KOMUNIKATY---------------------------------------

    //RM  - zapisywanie otrzymanego komunikatu do RAM
    //Kuba metoda receiveMessage
    private void RM() {
        communication.receiveMessage();
    }

    //SM  - wysłanie komunikatu
    //Kuba metoda sendMessage
    private void SM(String[] order) {
        int PID = Integer.parseInt(order[1]);
        Sms sms = new Sms(order[2]);
        communication.sendMessage(PID, sms);
    }

    //------------------------------------------------------------------------------

    public void Exe() throws Exception {
        String raw_order = processManager.getCommand(PC);
        System.out.println("Order: " + raw_order);
        DownloadRegisters();
        String[] order = raw_order.split(" ");
        String operation = order[0];

        try {
            switch (operation) {
                //------------------------ARYTMETYCZNO-LOGICZNE----------------------------------
                case "AD":
                    AD(order);
                    break;
                case "AX":
                    AX(order);
                    break;
                case "SB":
                    SB(order);
                    break;
                case "SX":
                    SX(order);
                    break;
                case "DC":
                    DC(order);
                    break;
                case "IC":
                    IC(order);
                    break;
                case "MU":
                    MU(order);
                    break;
                case "MX":
                    MX(order);
                    break;
                case "DV":
                    DV(order);
                    break;
                case "DX":
                    DX(order);
                    break;
                case "MV":
                    MV(order);
                    break;
                case "MZ":
                    MZ(order);
                    break;
                case "MO":
                    MO(order);
                    break;
                case "MY":
                    MY(order);
                    break;
                case "JP":
                    JP(order);
                    break;
                case "JZ":
                    JZ(order);
                    break;
                //wyswietlanie wyniku operacji
                case "PE":
                    PE(order);
                    break;
                //---------------------------------PROCESY---------------------------------------
                case "DP":
                    DP(order);
                    break;
                case "RP":
                    RP(order);
                    break;
                //-----------------------------------PLIKI---------------------------------------
                case "CE":
                    CE(order);
                    break;
                case "OF":
                    OF(order);
                    break;
                case "CL":
                    CL(order);
                    break;
                case "CF":
                    CF(order);
                    break;
                case "AF":
                    AF(order);
                    break;
                case "DF":
                    DF(order);
                    break;
                case "RF":
                    RF(order);
                    break;
                case "RN":
                    RN(order);
                    break;
                //-----------------------------KOMUNIKATY---------------------------------------
                case "RM":
                    RM();
                    break;
                case "SM":
                    SM(order);
                    break;

                case "EX":
                    processManager.getActivePCB().setState(PCB.State.FINISHED);
                    break;
                default:
                    System.out.println("Undefined order.");
                    break;
            }
        } catch (Exception e) {
            PC++;
            SaveRegister();
            throw e;
        } finally {
            PC++;
            SaveRegister();
            RegisterStatus();
        }
    }
}
