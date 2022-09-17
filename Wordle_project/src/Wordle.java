import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*; 

import javax.swing.*;

public class Wordle extends JFrame 
{
	private Panel[][] panels; 
	
	private int currentRow; 
	private int currentCol; 
	private boolean rowEntered; 
	private boolean isGameOver; 
	private boolean isWinner; 
	
	private String word; 
	private ArrayList<String> activeEntries; 
	private ArrayList<String> savedEntries; 
	private ArrayList<String> words;
	
	public Wordle() 
	{
		super("Wordle");
		this.setSize(600, 600);
		this.setResizable(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLayout(new GridLayout(6, 6));
		this.setBackground(Color.WHITE);
		setUpPanels(); 
		setVisible(true); 
		
		activeEntries = new ArrayList<String>(); 
		savedEntries = new ArrayList<String>(); 
		words = new ArrayList<String>(1000);
		
		word = getRandomWord(); 
		currentRow = 0; 
		currentCol = 0; 
		rowEntered = false; 
		isGameOver = false; 
		isWinner = false; 
		
		addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e)
			{
				if (isGameOver)
					endGame(); 
				
				int keyPressed = e.getKeyCode(); 
		
				if (keyPressed >= 'A' && keyPressed <= 'Z' && activeEntries.size() < 5)
				{
					activeEntries.add("" + (char)keyPressed);
					update(); 
				}
				if (keyPressed == 10 && activeEntries.size() == 5)
				{
					if(words.contains(getEntryAsString()))
					{
						rowEntered = true; 
						update(); 
					}
					else
						showInvalidMessage(); 
				}
				if (keyPressed == 8 && activeEntries.size() > 0)
				{
					activeEntries.remove(activeEntries.size() - 1);
					update(); 
				}
				System.out.println(keyPressed + " is " + (char)keyPressed);
			}
		});
	}
	
	private void setUpPanels()
	{
		panels = new Panel[6][5];
		
		for(int r = 0; r < panels.length; r++)
		{
			for(int c = 0; c < panels[r].length; c++)
			{
				Panel panel = new Panel(getWidth() / 5, getHeight() / 6); 
				panels[r][c] = panel;  
				
				this.add(panel); 
			}
		}
	}
	
	private void update()
	{
		if (rowEntered)
		{	
			showResult(); 
			
			for(String s: activeEntries)
				savedEntries.add(s); 
			activeEntries.clear(); 
			rowEntered = false; 
		}	
		else if (isGameOver)
			endGame(); 
		else
			showCurrentRow(); 
	}
	
	private boolean[] getMatches()
	{
		boolean[] matches = new boolean[5];
		
		for(int i = 0; i < 5 && i < activeEntries.size(); i++)
		{
			if(activeEntries.get(i).equals(word.substring(i, i + 1)))
			{
				matches[i] = true; 
			}
		}
		
		return matches; 
	}
	
	private boolean[] getPartialMatches()
	{
		boolean[] matches = getMatches(); 
		boolean[] partial = new boolean[5]; 
		
		for(int i = 0; i < 5 && i < activeEntries.size(); i++)
		{
			for(int j = 0; j < 5 && i < activeEntries.size(); j++)
			{
				if(i != j && !matches[i] && !matches[j] &&
					activeEntries.get(i).equals(word.substring(j, j + 1)))
						partial[i] = true; 
			}
		}
		
		return partial; 
	}
	
	public void showResult()
	{
		boolean[] matches = getMatches(); 
		boolean[] partial = getPartialMatches(); 
		Panel[] roPanel = panels[currentRow]; 
		int matchCount = 0; 
		
		for (int i = 0; i < 5; i++)
		{
			Panel current = roPanel[i]; 
			
			if (matches[i])
			{
				current.setBackgroundColor(new Color(1, 154, 1));
				current.setTextColor(Color.WHITE);
				matchCount++; 
			}
			else if (partial[i])
			{
				current.setBackgroundColor(new Color(255, 196, 37));
				current.setTextColor(Color.WHITE);
			}
			else
			{
				current.setBackgroundColor(Color.GRAY);
				current.setTextColor(Color.WHITE);
			}
		}
		
		currentRow++; 
		
		if(matchCount == 5)
			isWinner = true; 
		if(matchCount == 5 || currentRow > 5)
		{
			isGameOver = true; 
			endGame(); 
		}
	}
	
	public void showCurrentRow()
	{
		Panel[] roPanel = panels[currentRow];
		
		for(int i = 0; i < roPanel.length; i++)
		{
			if (i < activeEntries.size())
				roPanel[i].setLetter(activeEntries.get(i)); 
			else
				roPanel[i].setLetter(""); 
		}
	}
	
	public String getRandomWord() 
	{
		String str = ""; 
		
		try 
		{
			File file = new File("fiveLetterWords.txt");
			Scanner reader = new Scanner(file); 
			
			while(reader.hasNext())
				words.add(reader.nextLine().toUpperCase()); 
		
			int rand = (int)(Math.random() * words.size()); 
		
			str = words.get(rand); 
		}
		catch (FileNotFoundException obj)
		{
			
		}
			
		if(str.equals(""))
			return "ERROR"; 
		else
			return str; 
		
	}
	
	private String getEntryAsString()
	{
		String str = "";
		
		for(String s: activeEntries)
			str += s.charAt(0); 
		
		return str; 
	}
	
	public void showInvalidMessage()
	{
		PopUpWindow message = new PopUpWindow(); 
		message.showNotValidWordMessage();
	}
	
	public void endGame()
	{
		PopUpWindow message = new PopUpWindow(); 
		
		if(isWinner)
		{
			message.showWinMessage();
			//System.out.println("Impressive!");
		}
		else
		{
			message.showLoseMessage(word);
			//System.out.println("Game Over"); 
			//System.out.println("The word was " + word); 
		}
		
		addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e)
			{
				int keyPressed = e.getKeyCode(); 
		
				if (keyPressed >= 'A' && keyPressed <= 'Z' && activeEntries.size() < 5)
				{
				}
				if (keyPressed == 10 && activeEntries.size() == 5)
				{
				}
				if (keyPressed == 8 && activeEntries.size() > 0)
				{
				}
			}
		});
	}
	
	
	

	
	
	public static void main(String[] args)
	{
		Wordle g = new Wordle(); 
		

	}
	
	
}
