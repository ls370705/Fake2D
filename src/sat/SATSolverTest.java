package sat;

import com.sun.jdi.event.ClassUnloadEvent;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import sat.env.Bool;
import sat.env.Environment;
import sat.env.Variable;
import sat.formula.Clause;
import sat.formula.Formula;
import sat.formula.Literal;
import sat.formula.NegLiteral;
import sat.formula.PosLiteral;
import sudoku.Main;

public class SATSolverTest {
    Literal a = PosLiteral.make("a");
    Literal b = PosLiteral.make("b");
    Literal c = PosLiteral.make("c");
    Literal na = a.getNegation();
    Literal nb = b.getNegation();
    Literal nc = c.getNegation();

    // make sure assertions are turned on!  
    // we don't want to run test cases without assertions too.
    // see the handout to find out how to turn them on.
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false;
    }

//    @Test
//    public void testSolve001() {
//    	Variable a = new Variable("a");
//    	Variable b = new Variable("b");
//    	Literal la = PosLiteral.make(a);
//    	Literal lb = PosLiteral.make(b);
//    	Literal nlb = lb.getNegation();
//    	Clause c1 = makeClause(la, nlb);
//    	Clause c2 = makeClause(la, lb);
//    	Formula f = makeFormula(c1, c2);
//    	Environment env = SATSolver.solve(f);
//    	assertEquals(env.get(a), Bool.TRUE);
//    	assertEquals(env.get(b), Bool.TRUE);
//    }
//
//    @Test
//    public void testSolve002() {
//    	Variable a = new Variable("a");
//    	Variable b = new Variable("b");
//    	Literal la = PosLiteral.make(a);
//    	Literal lb = PosLiteral.make(b);
//    	Literal lnb = lb.getNegation();
//    	Clause cla = makeClause(la);
//    	Clause clb = makeClause(lb);
//    	Clause clnb = makeClause(lnb);
//    	Formula f1 = makeFormula(cla, clb);
//    	Formula f2 = makeFormula(cla, clnb);
//    	Environment env = SATSolver.solve(f1.and(f2));
//    	assertNull(env);
//    }
//
//    @Test
//    public void testSolve003() {
//    	Variable a = new Variable("a");
//    	Variable b = new Variable("b");
//    	Variable c = new Variable("c");
//    	Literal la = PosLiteral.make(a);
//    	Literal lb = PosLiteral.make(b);
//    	Literal lnb = lb.getNegation();
//    	Literal lc = PosLiteral.make(c);
//    	Clause cla = makeClause(la);
//    	Clause clb = makeClause(lb);
//    	Clause clnbc = makeClause(lnb, lc);
//    	Formula f1 = makeFormula(cla, clb);
//    	Formula f2 = makeFormula(clnbc);
//    	Environment env = SATSolver.solve(f1.and(f2));
//    	assertEquals(env.get(a), Bool.TRUE);
//    	assertEquals(env.get(b), Bool.TRUE);
//    	assertEquals(env.get(c), Bool.TRUE);
//    }
//
//    @Test
//    public void testSolve004() {
//    	Variable a = new Variable("a");
//    	Literal la = PosLiteral.make(a);
//    	Literal lna = la.getNegation();
//    	Clause cla = makeClause(la);
//    	Clause clna = makeClause(lna);
//    	Formula f = makeFormula(cla, clna);
//    	Environment env = SATSolver.solve(f);
//    	assertNull(env);
//    }
//
//    @Test
//    public void testSolve005() {
//		Variable a = new Variable("a");
//		Variable b = new Variable("b");
//		Variable c = new Variable("c");
//		Literal la = PosLiteral.make(a);
//		Literal lb = PosLiteral.make(b);
//		Literal lnb = lb.getNegation();
//		Literal lc = PosLiteral.make(c);
//		Clause c1 = makeClause(la, lnb);
//		Clause c2 = makeClause(la, lc);
//		Formula f = makeFormula(c1, c2);
//		Environment env = SATSolver.solve(f);
//		assertEquals(env.get(a), Bool.TRUE);
//		assertEquals(env.get(b), Bool.TRUE);
//		assertEquals(env.get(c), Bool.TRUE);
//	}
    
    // Helper function for constructing a formula.  Takes
    // a variable number of arguments, e.g.
    // makeFormula(a, b, c) will make the formula (a and b and c)
    // @param e,...   clause in the formula
    // @return formula containing e,...
    private  static Formula makeFormula(Clause[] e) {
        Formula f = new Formula();
        for (int i = 0; i < e.length; ++i) {
            f = f.addClause(e[i]);
        }
        return f;
    }
    
    // Helper function for constructing a clause.  Takes
    // a variable number of arguments, e.g.
    // make(a, b, c) will make the clause (a or b or c)
    // @param e,...   literals in the clause
    // @return clause containing e,...
    private static Clause makeClause(Literal[] e) {
        Clause c = new Clause();
        for (int i = 0; i < e.length; ++i) {
            c = c.add(e[i]);
        }
        return c;
    }

	public static void main(String[] args){
		File file = new File("/Users/liusu/Desktop/s8Sat.cnf");
		BufferedReader reader = null;
		List<Clause> clauses = new ArrayList<Clause>();
		List<Variable> variables = new ArrayList<>();
		List<Literal> posLiterals = new ArrayList<>();
		List<Literal> negLiterals = new ArrayList<>();
		try{
			reader = new BufferedReader(new FileReader(file));
			String temp = null;
			int line = 1;
			int numberOfVariables = 0;
			int numberOfClauses = 0;
			while ((temp = reader.readLine()) != null) {
				if(line == 2){
					String[] info = temp.split(" ");
					numberOfVariables = Integer.parseInt(info[2]);
//					numberOfClauses = Integer.parseInt(info[3]);
					for(int i = 1 ; i < numberOfVariables + 1 ; i++){
						variables.add(new Variable("x"+i));
						posLiterals.add(PosLiteral.make(variables.get(i - 1)));
						negLiterals.add(NegLiteral.make(variables.get(i - 1)));
					}
				}else if(line > 2){
					if(!temp.equals("\n")){
						String[] info = temp.split(" ");
						int temp1;
						Literal[] literals = new Literal[info.length - 1];
						for(int i = 0 ; i < info.length - 1 ; i++){
							temp1 = Integer.parseInt(info[i]);
							if(temp1 > 0){
								literals[i] = posLiterals.get(Math.abs(temp1) - 1);
							}else{
								literals[i] = negLiterals.get(Math.abs(temp1) - 1);
							}
						}
						clauses.add(makeClause(literals));
					}
				}
				line++;
			}
			Clause[] newClauses = new Clause[clauses.size()];
			clauses.toArray(newClauses);
			Formula f = makeFormula(newClauses);
			Environment env = SATSolver.solve(f);
			reader.close();
			System.out.println(env);
		}catch (IOException e){
            System.out.println("Error");
            e.printStackTrace();
		}
	}
}