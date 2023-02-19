package game;

import city.cs.engine.*;


public class Student extends Walker {
	private static final Shape STUDENT_SHAPE = new BoxShape(1,2);
	private static final BodyImage IMAGE = new BodyImage("/home/marcel/Projects/IntelliJ/javaproject2023-Supermarcel10/data/img/student.png", 4);
	private int credits = 0;


	public Student(World world) {
		super(world, STUDENT_SHAPE);

		this.addImage(IMAGE);

	}

	public int getCredits() {
		return credits;
	}

	public void setCredits(int credits) {
		this.credits = credits;
	}
}
