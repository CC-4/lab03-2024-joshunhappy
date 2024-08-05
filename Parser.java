/*
    Laboratorio No. 3 - Recursive Descent Parsing
    CC4 - Compiladores

    Clase que representa el parser

    Actualizado: agosto de 2021, Luis Cu
*/

import java.util.LinkedList;
import java.util.Stack;

public class Parser {

    // Puntero next que apunta al siguiente token
    private int next;
    // Stacks para evaluar en el momento
    private Stack<Double> operandos;
    private Stack<Token> operadores;
    // LinkedList de tokens
    private LinkedList<Token> tokens;

    // Funcion que manda a llamar main para parsear la expresion
    public boolean parse(LinkedList<Token> tokens) {
        this.tokens = tokens;
        this.next = 0;
        this.operandos = new Stack<Double>();
        this.operadores = new Stack<Token>();

        // Recursive Descent Parser
        // Imprime si el input fue aceptado
        System.out.println("Aceptada? " + S());

        // Shunting Yard Algorithm
        // Imprime el resultado de operar el input
        System.out.println("Resultado: " + this.operandos.peek());

        // Verifica si terminamos de consumir el input
        if (this.next != this.tokens.size()) {
            return false;
        }
        return true;
    }

    // Verifica que el id sea igual que el id del token al que apunta next
    // Si si avanza el puntero es decir lo consume.
    private boolean term(int id) {
        if (this.next < this.tokens.size() && this.tokens.get(this.next).equals(id)) {

            Token curentToken = this.tokens.get(this.next);

            // Codigo para el Shunting Yard Algorithm

            if (id == Token.NUMBER) {
                // Encontramos un numero
                // Debemos guardarlo en el stack de operandos
                operandos.push(this.tokens.get(this.next).getVal());

            } else if (id == Token.SEMI) {
                // Encontramos un punto y coma
                // Debemos operar todo lo que quedo pendiente
                while (!this.operadores.empty()) {
                    popOp();
                }
            } else if (isOperator(curentToken)) {
                // Encontramos algun otro token, es decir un operador
                // Lo guardamos en el stack de operadores
                // Que pushOp haga el trabajo, no quiero hacerlo yo aqui
                pushOp(curentToken);
            }

            // Next line for debuggin
            // System.out.println("Token: " + this.tokens.get(this.next) + ", Expected ID: "
            // + id);

            this.next++;

            return true;
        }

        // Next line for debuggin
        // System.out
        // .println(
        // "Token unsuccesful, Expecte ID: " + id + ", Found ID: " +
        // this.tokens.get(this.next).getId());

        return false;
    }

    private boolean isOperator(Token token) {
        return token.getId() == Token.PLUS
                || token.getId() == Token.MINUS
                || token.getId() == Token.MULT
                || token.getId() == Token.DIV
                || token.getId() == Token.MOD
                || token.getId() == Token.EXP;
    }

    // Funcion que verifica la precedencia de un operador
    private int pre(Token op) {
        /* TODO: Su codigo aqui */

        /* El codigo de esta seccion se explicara en clase */

        switch (op.getId()) {
            case Token.PLUS:
            case Token.MINUS:
                return 1;
            case Token.MULT:
            case Token.DIV:
            case Token.MOD:
                return 2;
            case Token.EXP:
                return 3;
            default:
                return -1;
        }
    }

    private void popOp() {
        Token op = this.operadores.pop();

        /* TODO: Su codigo aqui */

        // Pop b first to preserver entry order
        double b = this.operandos.pop();
        double a = this.operandos.pop();

        /* El codigo de esta seccion se explicara en clase */

        System.out.println(op.getId() + ": " + a + op + b);

        switch (op.getId()) {
            case Token.PLUS:
                operandos.push(a + b);
                break;
            case Token.MINUS:
                operandos.push(a - b);
                break;
            case Token.MULT:
                operandos.push(a * b);
                break;
            case Token.DIV:
                operandos.push(a / b);
                break;
            case Token.MOD:
                operandos.push(a % b);
                break;
            case Token.EXP:
                operandos.push(Math.pow(a, b));
                break;
            default:
                throw new IllegalArgumentException("Uknonw operator: " + op);
        }
    }

    private void pushOp(Token op) {
        /* TODO: Su codigo aqui */

        /* Casi todo el codigo para esta seccion se vera en clase */

        // Si si hay operandos:
        // Obtenemos la precedencia de op
        // Obtenemos la precedencia de quien ya estaba en el stack
        // Comparamos las precedencias y decidimos si hay que operar
        // Es posible que necesitemos un ciclo aqui, una vez tengamos varios niveles de
        // precedencia
        // Al terminar operaciones pendientes, guardamos op en stack

        while (!operadores.empty() && pre(operadores.peek()) >= pre(op)) {
            popOp();
        }

        // Si no hay operandos automaticamente ingresamos op al stack
        operadores.push(op);
    }

    private boolean S() {
        // Next line for debuggin
        // System.out.println("S() - next: " + this.next);

        return A() && term(Token.SEMI);
    }

    private boolean A() {
        // Next line for debuggin
        // System.out.println("A() - next: " + this.next);

        return A1();
    }

    private boolean A1() {
        // Next line for debuggin
        // System.out.println("A1() - next: " + this.next);

        return C() && B();
    }

    private boolean C() {
        // Next line for debuggin
        // System.out.println("C() - next: " + this.next);

        return C1();
    }

    private boolean C1() {
        // Next line for debuggin
        // System.out.println("C1() - next: " + this.next);

        return E() && D();
    }

    private boolean B() {
        // Next line for debuggin
        // System.out.println("B() - next: " + this.next);

        int save = next;

        if (B1()) {
            return true;
        }

        this.next = save;

        if (B2()) {
            return true;
        }

        this.next = save;

        return true;
    }

    private boolean B1() {
        // Next line for debuggin
        // System.out.println("B1() - next: " + this.next);

        return term(Token.PLUS) && C() && B();
    }

    private boolean B2() {
        // Next line for debuggin
        // System.out.println("B2() - next: " + this.next);

        return term(Token.MINUS) && C() && B();
    }

    private boolean D() {
        // Next line for debuggin
        // System.out.println("D() - next: " + this.next);

        int save = next;

        if (D1()) {
            return true;
        }

        this.next = save;

        if (D2()) {
            return true;
        }

        this.next = save;

        if (D3()) {
            return true;
        }

        this.next = save;

        return true; // B puede ser vacío
    }

    private boolean D1() {
        // Next line for debuggin
        // System.out.println("D1() - next: " + this.next);

        return term(Token.MULT) && E() && D();
    }

    private boolean D2() {
        // Next line for debuggin
        // System.out.println("D2() - next: " + this.next);

        return term(Token.DIV) && E() && D();
    }

    private boolean D3() {
        // Next line for debuggin
        // System.out.println("D3() - next: " + this.next);

        return term(Token.MOD) && E() && D();
    }

    private boolean E() {
        // Next line for debuggin
        // System.out.println("E() - next: " + this.next);

        return E1();
    }

    private boolean E1() {
        // Next line for debuggin
        // System.out.println("E1() - next: " + this.next);

        return G() && F();
    }

    private boolean F() {
        // Next line for debuggin
        // System.out.println("F() - next: " + this.next);

        int save = next;

        if (F1()) {
            return true;
        }

        this.next = save;

        return true;
    }

    private boolean F1() {
        // Next line for debuggin
        // System.out.println("F1() - next: " + this.next);

        return term(Token.EXP) && E();
    }

    private boolean G() {
        // Next line for debuggin
        // System.out.println("G() - next: " + this.next);

        int save = next;

        if (G1()) {
            return true;
        }

        this.next = save;

        if (G2()) {
            return true;
        }

        this.next = save;

        return G3();
    }

    private boolean G1() {
        // Next line for debuggin
        // System.out.println("G1() - next: " + this.next);

        return term(Token.LPAREN) && A() && term(Token.RPAREN);
    }

    private boolean G2() {
        // Next line for debuggin
        // System.out.println("G2() - next: " + this.next);

        return term(Token.UNARY) && G();
    }

    private boolean G3() {
        // Next line for debuggin
        // System.out.println("G3() - next: " + this.next);

        return N();
    }

    private boolean N() {
        return term(Token.NUMBER);
    }
}
