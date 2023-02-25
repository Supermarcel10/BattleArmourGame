package game;

import city.cs.engine.CircleShape;
import city.cs.engine.DynamicBody;
import city.cs.engine.UserView;
import org.jetbrains.annotations.NotNull;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


public class GiveFocus implements MouseListener {
	private UserView view;

	public GiveFocus(UserView v){
		this.view = v;
	}


	@Override
	public void mouseClicked(@NotNull MouseEvent e) {
		System.out.printf("Mouse button %d clicked at %d, %d%n", e.getButton(), e.getX(), e.getY());

		DynamicBody ball = new DynamicBody(view.getWorld(), new CircleShape(1f));
		ball.setPosition(view.viewToWorld(e.getPoint()));
	}


	@Override
	public void mousePressed(MouseEvent e) {

	}


	@Override
	public void mouseReleased(MouseEvent e) {

	}


	@Override
	public void mouseEntered(MouseEvent e) {
		view.requestFocus();
	}


	@Override
	public void mouseExited(MouseEvent e) {
	}
}