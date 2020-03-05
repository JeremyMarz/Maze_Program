package mazePD;

import java.util.ArrayList;
import java.util.Stack;


import mazePD.Maze;
import mazePD.Maze.Content;
import mazePD.Coordinates;

public class MazeTest {
	public static void main(String[] args) {
		System.out.println("Start Maze");
		Stack <Coordinates> stack = new Stack<Coordinates>();
		ArrayList<String> prevLoc = new ArrayList<String>(); 
		Maze maze = new Maze(5, 3, Maze.MazeMode.TEST);
		Droid droid = new Droid("C3PO");
		Maze.Direction nextDirection; 
		
		maze.enterMaze(droid);
		
		for (int i = 0; i < maze.getMazeDepth(); i++)
		{
			System.out.println("LEVEL: " + i);
			String[] level = maze.toStringLevel(i);
			for(int j = 0; j < level.length; j++) 
			{
				System.out.println(level[j]);
			}
		}
		
		System.out.println("-------");
		System.out.println("START AT:"+maze.getCurrentCoordinates(droid).toString());
		
		boolean solved = false;
		boolean down = false;
		boolean advance = false;
		
	
		while(!solved)
		{
			prevLoc.clear(); 
			stack.clear();
			
			stack.push(maze.getCurrentCoordinates(droid)); //push starting location onto stack
			prevLoc.add(maze.getCurrentCoordinates(droid).toString()); //add to previous location
			
			while(maze.scanCurLoc(droid) != Maze.Content.END && !stack.isEmpty())
			{
				if(stack.peek() != maze.getCurrentCoordinates(droid)) { //check top of stack to see if droid is in the correct spot
					nextDirection = findDirection(stack.peek(), maze.getCurrentCoordinates(droid)); //find next direction
					maze.move(droid, nextDirection); //move droid
				}
				
				nextDirection = getmyLocation(maze, droid, prevLoc);//set next direction as my current location
				
				if(nextDirection != null) //if there is a next direction
				{
					maze.move(droid, nextDirection); //move droid
					prevLoc.add(maze.getCurrentCoordinates(droid).toString()); //add move to prevous location
					stack.push(maze.getCurrentCoordinates(droid)); // push new location to stack
				}
				else
				{
					stack.pop(); //pop location if there is no location
					if(maze.scanCurLoc(droid) == Maze.Content.PORTAL_DN) //if droid is over the portal
					{
						advance = true; 
					}
					
					if(stack.isEmpty()) // if the stack is empty
					{
					System.out.println("Maze searched");
					System.out.println("-------");

					}
				}
				System.out.println(maze.getCurrentCoordinates(droid).toString());
				
				if(maze.scanCurLoc(droid) == Maze.Content.END) //if droid is at the end
				{
					solved = true;
				}
			}
			
			prevLoc.clear();
			
			stack.push(maze.getCurrentCoordinates(droid));
			prevLoc.add(maze.getCurrentCoordinates(droid).toString());
			
			while(maze.scanCurLoc(droid) != Maze.Content.PORTAL_DN && !stack.isEmpty() && !solved && !down) //checks for a portal
			{
				if(stack.peek() != maze.getCurrentCoordinates(droid)) { //looks to see droid is at a valid location
					nextDirection = findDirection(stack.peek(), maze.getCurrentCoordinates(droid));
					maze.move(droid, nextDirection); 
				}
				
				nextDirection = getmyLocation(maze, droid, prevLoc);
				
				if(nextDirection == null) //if the nextDirection isn't found then pop the stack
				{
					stack.pop();
				}
				else 
				{
					maze.move(droid, nextDirection); //if there is a new direction the move
					prevLoc.add(maze.getCurrentCoordinates(droid).toString()); 
					stack.push(maze.getCurrentCoordinates(droid)); 
				}
				System.out.println(maze.getCurrentCoordinates(droid).toString()); //droid tries to go down at the location

				if(maze.scanCurLoc(droid) == Maze.Content.PORTAL_DN)
				{
					advance = true;
					System.out.println("-------");

					System.out.println("Portal Location:"+ maze.getCurrentCoordinates(droid).toString()); //droid tries to go down at the location

				}
				
			}
			
			if(advance)
			{
				maze.usePortal(droid, Maze.Direction.DN);
				System.out.println("-------");
				if(!solved) {
				System.out.println("Entering Portal....");
				System.out.println("-------");
				System.out.println("Next Level Current Location:"+ maze.getCurrentCoordinates(droid).toString());
				}
				else
				{
					System.out.println("Exiting Maze");
				}
			}
			else
			{
				down = true; 
				stack.push(maze.getCurrentCoordinates(droid));
				prevLoc.add(maze.getCurrentCoordinates(droid).toString());
			}
		}
		
		if(solved)
		{
			System.out.println("-------");
			System.out.println("Solved!");
		}
		
	}
		public static Maze.Direction findDirection(Coordinates next, Coordinates prev)
		{
			Maze.Direction to = null;
			
			if(next.getX() == (prev.getX() + 1)) //RIGHT
			{ 
			to = Maze.Direction.D90;
			}
			else if(next.getX() == (prev.getX() - 1)) //LEFT 
			{ 
			to = Maze.Direction.D270;
			}
			else if(next.getY() == (prev.getY() - 1)) //DOWN
			{
				to = Maze.Direction.D00;
			}
			else if(next.getY() == (prev.getY() + 1)) //UP
			{
				to = Maze.Direction.D180;
			}	
			return to;
		}
		
		
		public static Maze.Direction getmyLocation(Maze maze, Droid droid, ArrayList<String> prev)
		{
			Maze.Direction direction = null;
			
			
			Content[] neighborContent = maze.scanAdjLoc(droid);
			String[] neighborCoordinates = new String[4];
			
			Boolean moved = false;
			
			int[][] offset= {{0,-1},{1,0},{0,1},{-1,0}}; //offset to get the location of the cells next to the droid
			Coordinates droidLoc = maze.getCurrentCoordinates(droid);
			
			
			for (int i = 0;i < 4; i++)
			{
				int adjX = droidLoc.getX()+offset[i][0];
				int adjY = droidLoc.getY()+offset[i][1];
				
				
				neighborCoordinates[i] = new Coordinates(adjX, adjY, droidLoc.getZ()).toString();
			}
			System.out.println("-------");
			
			for (int i = 0; i < 4 && !moved; i++)
			{
			
				
				if(isValid(neighborContent[i]) && !(prev.contains(neighborCoordinates[i])))
				{
					if(i == 0)
					{
						direction = Maze.Direction.D00;
					}
					if(i == 1)
					{
						direction = Maze.Direction.D90;
					}
					if(i == 2)
					{
						direction = Maze.Direction.D180;
					}
					if(i == 3)
					{
						direction = Maze.Direction.D270;
					}
					moved = true;
				}
			}
				
			return direction;
		}
		

		public static boolean isValid(Maze.Content name) //check if it a valid location for droid
		{
			
			if (name == Maze.Content.EMPTY || name == Maze.Content.END || name == Maze.Content.PORTAL_UP || name == Maze.Content.PORTAL_DN || name == Maze.Content.COIN)
			{
				return true;
			}
			else
			{
				return false;
			}
		}
}
