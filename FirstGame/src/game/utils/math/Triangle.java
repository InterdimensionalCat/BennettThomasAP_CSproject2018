package game.utils.math;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import game.entity.Player;

public class Triangle {
	
	private Point a;
	private Point b;
	private Point c;
	private int[] xPoints;
	private int[] yPoints;
	private Point[] hPoints;
	
	public Triangle(Point a, int b, double angle) {
		hPoints = new Point[b];
		this.a = a;
		this.b = new Point((int)a.getX() + b, (int)a.getY());
		this.c = new Point((int)this.b.getX(), (int)(this.b.getY() - (b*Math.round(Math.tan(angle)))));
		int[] xPoints = { (int)this.a.getX(),(int)this.b.getX(),(int)this.c.getX() };
		int[] yPoints = { (int)this.a.getY(),(int)this.b.getY(),(int)this.c.getY() };
		this.xPoints = xPoints;
		this.yPoints = yPoints;
		for(int i = 1; i <= b; i++) {
			hPoints[i-1] = new Point((int)this.a.getX() + i, (int)this.a.getY() - (int)(i*Math.tan(angle)));
		}
	}

	public boolean intersects(double x, double y, Player player) {
		if(x > a.getX() && x < b.getX() && y <= a.getY()) {
			if(hPoints[(int)(x-(a.getX())) - 1].getY() < y ) {
				//player.setY(hPoints[(int)(x-(a.getX())) - 1].getY() - 64);
				return true;
			}
		}
		return false;
	}
	
	public Point[] getHPoints() {
		return hPoints;
	}
	
	public Point getA() {
		return a;
	}

	public Point getB() {
		return b;
	}

	public Point getC() {
		return c;
	}

	public void render(Graphics2D g2d, int offsetX, int offsetY) {
		for(int i = 0; i < xPoints.length; i++) {
			xPoints[i] += offsetX;
		}
		
		for(int i = 0; i < yPoints.length; i++) {
			yPoints[i] += offsetY;
		}
		g2d.drawPolygon(xPoints, yPoints, 3);
		for(int i = 0; i < xPoints.length; i++) {
			xPoints[i] -= offsetX;
		}
		
		for(int i = 0; i < yPoints.length; i++) {
			yPoints[i] -= offsetY;
		}
	}
}
