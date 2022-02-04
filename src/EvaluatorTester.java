

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author mandynoto
 */
public class EvaluatorTester {
    public static void main(String[] args) {
        Evaluator anEvaluator = new Evaluator();
        for (String arg : args) {
            System.out.println(arg + " = " + anEvaluator.eval(arg));
        }
    }
}