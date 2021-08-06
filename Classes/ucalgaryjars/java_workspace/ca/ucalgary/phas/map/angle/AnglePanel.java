// 
// Decompiled by Procyon v0.5.36
// 

package ca.ucalgary.phas.map.angle;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentAdapter;
import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.Point;
import javax.swing.event.MouseInputAdapter;
import java.awt.event.ActionEvent;
import javax.swing.AbstractButton;
import ca.ucalgary.phas.map.utilities.MapButtonPropertySetter;
import javax.swing.JMenuItem;
import java.awt.event.ActionListener;
import javax.swing.JPopupMenu;
import ca.ucalgary.phas.map.utilities.MapMath;
import ca.ucalgary.phas.map.utilities.vectors.VectorUtil;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.event.ComponentListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseListener;
import java.awt.Component;
import java.awt.BasicStroke;
import java.text.DecimalFormat;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Line2D;
import java.awt.geom.Arc2D;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JPanel;

public class AnglePanel extends JPanel
{
    private static final double POINT_RADIUS = 3.5;
    private Angle parent;
    private Font screenFont;
    private Color fontColor;
    private Color arcColor;
    private Color pointColor;
    private Arc2D.Double arc;
    private Line2D.Double leg1;
    private Line2D.Double leg2;
    private Point2D.Double center;
    private Ellipse2D.Double elL1;
    private Ellipse2D.Double elL2;
    private Ellipse2D.Double elRadius;
    private Point2D.Double ptL1;
    private Point2D.Double ptL2;
    private Point2D.Double ptRadius;
    private boolean onL1;
    private boolean onL2;
    private boolean onRadius;
    private double panelWidth;
    private double panelHeight;
    private double radius;
    private double theta;
    private double span;
    private DecimalFormat df;
    private Point2D.Double ptThetaLabel;
    private Point2D.Double ptRadiusLabel;
    private Point2D.Double ptSpanLabel;
    private BasicStroke stroke;
    private Label2 lblL1;
    private Label2 lblL2;
    private Label2 lblTheta;
    private Label2 lblSpan;
    private Label2 lblRadius;
    protected Popup myPopup;
    
    public AnglePanel(final Angle parent) {
        this.screenFont = new Font("Dialog", 1, 12);
        this.fontColor = Color.black;
        this.arcColor = Color.blue;
        this.pointColor = Color.red;
        this.arc = new Arc2D.Double(2);
        this.leg1 = new Line2D.Double();
        this.leg2 = new Line2D.Double();
        this.center = new Point2D.Double();
        this.elL1 = new Ellipse2D.Double();
        this.elL2 = new Ellipse2D.Double();
        this.elRadius = new Ellipse2D.Double();
        this.ptL1 = new Point2D.Double();
        this.ptL2 = new Point2D.Double();
        this.ptRadius = new Point2D.Double();
        this.onL1 = false;
        this.onL2 = false;
        this.onRadius = false;
        this.panelWidth = 0.0;
        this.panelHeight = 0.0;
        this.radius = 0.0;
        this.theta = 0.0;
        this.span = 0.0;
        this.df = new DecimalFormat(" 0.00;-0.00");
        this.ptThetaLabel = new Point2D.Double();
        this.ptRadiusLabel = new Point2D.Double();
        this.ptSpanLabel = new Point2D.Double();
        this.stroke = new BasicStroke(5.0f);
        this.lblL1 = new Label2("L1", this);
        this.lblL2 = new Label2("L2", this);
        this.lblTheta = new Label2("\u03b8", this);
        this.lblSpan = new Label2("s", this);
        this.lblRadius = new Label2("r", this);
        this.myPopup = new Popup();
        final MyMouseAdapter myMouseAdapter = new MyMouseAdapter(this);
        this.parent = parent;
        this.addMouseListener(myMouseAdapter);
        this.addMouseMotionListener(myMouseAdapter);
        this.addComponentListener(new MyComponentAdapter());
    }
    
    public void reset() {
        this.panelWidth = this.getWidth();
        this.panelHeight = this.getHeight();
        final double min = Math.min(this.panelWidth, this.panelHeight);
        this.center.x = this.panelWidth / 2.0;
        final Point2D.Double center = this.center;
        final Point2D.Double ptRadius = this.ptRadius;
        final double n = this.panelHeight / 2.0;
        ptRadius.y = n;
        center.y = n;
        this.ptRadius.x = this.center.x + min / 6.0;
        this.ptL1.x = this.center.x + min / 8.0;
        this.ptL1.y = this.center.y;
        this.ptL2.x = this.center.x;
        this.ptL2.y = this.center.y - min / 8.0;
        this.elL1.height = 7.0;
        this.elL1.width = 7.0;
        this.elL2.height = 7.0;
        this.elL2.width = 7.0;
        this.elRadius.width = 7.0;
        this.elRadius.height = 7.0;
        this.calculate();
    }
    
    public void paint(final Graphics graphics) {
        this.paint((Graphics2D)graphics);
    }
    
    public void paint(final Graphics2D graphics2D) {
        final BasicStroke basicStroke = (BasicStroke)graphics2D.getStroke();
        final Font font = graphics2D.getFont();
        final Color color = graphics2D.getColor();
        this.elL1.x = this.ptL1.x - 3.5;
        this.elL1.y = this.ptL1.y - 3.5;
        this.elL2.x = this.ptL2.x - 3.5;
        this.elL2.y = this.ptL2.y - 3.5;
        this.elRadius.x = this.ptRadius.x - 3.5;
        this.elRadius.y = this.ptRadius.y - 3.5;
        graphics2D.setFont(this.screenFont);
        graphics2D.setColor(Color.white);
        graphics2D.fillRect(0, 0, toInt(this.panelWidth), toInt(this.panelHeight));
        graphics2D.setStroke(this.stroke);
        graphics2D.setColor(this.arcColor);
        graphics2D.draw(this.arc);
        graphics2D.draw(this.leg1);
        graphics2D.draw(this.leg2);
        graphics2D.setColor(Color.white);
        graphics2D.draw(this.elRadius);
        graphics2D.draw(this.elL1);
        graphics2D.draw(this.elL2);
        graphics2D.setColor(this.pointColor);
        graphics2D.fill(this.elRadius);
        graphics2D.fill(this.elL1);
        graphics2D.fill(this.elL2);
        this.lblL1.setLoc(this.ptL1.x - this.lblL1.getWidth() / 2.0, this.ptL1.y + 7.0);
        this.lblL2.setLoc(this.ptL2.x - this.lblL2.getWidth() / 2.0, this.ptL2.y + 7.0);
        this.lblTheta.setLoc(this.ptThetaLabel);
        this.lblRadius.setLoc(this.ptRadius.x - this.lblRadius.getWidth() / 2.0, this.ptRadius.y + 7.0);
        this.lblSpan.setLoc(this.ptSpanLabel);
        graphics2D.setStroke(basicStroke);
        this.lblL1.paint(graphics2D);
        this.lblL2.paint(graphics2D);
        this.lblTheta.paint(graphics2D);
        this.lblRadius.paint(graphics2D);
        this.lblSpan.paint(graphics2D);
        graphics2D.setColor(this.fontColor);
        graphics2D.drawString("s : ", 5, 15);
        graphics2D.drawString(this.df.format(this.span / 10.0) + " m", 50, 15);
        graphics2D.drawString("r : ", 5, 30);
        graphics2D.drawString(this.df.format(this.radius / 10.0) + " m", 50, 30);
        graphics2D.drawString("\u03b8 = s/r : ", 5, 45);
        graphics2D.drawString(this.df.format(this.theta) + " rad", 50, 45);
        graphics2D.setColor(color);
        graphics2D.setFont(font);
        graphics2D.setStroke(basicStroke);
    }
    
    private void calculate() {
        final double angle = VectorUtil.findAngle(this.center, this.ptL1);
        final double angle2 = VectorUtil.findAngle(this.center, this.ptL2);
        this.radius = this.ptRadius.distance(this.center);
        this.arc.y = this.center.y - this.radius;
        this.arc.x = this.center.x - this.radius;
        final Arc2D.Double arc = this.arc;
        final Arc2D.Double arc2 = this.arc;
        final double abs = Math.abs(this.radius * 2.0);
        arc2.width = abs;
        arc.height = abs;
        this.arc.start = Math.toDegrees(VectorUtil.findAngle(this.center, this.ptL1));
        this.arc.extent = Math.toDegrees(MapMath.findPosAngle(this.center, this.ptL1, this.ptL2));
        this.ptThetaLabel = VectorUtil.translatePoint2D(this.center, Math.toRadians(this.arc.start + this.arc.extent / 2.0), this.radius / 2.0);
        this.ptRadiusLabel = VectorUtil.translatePoint2D(this.center, VectorUtil.findAngle(this.center, this.ptRadius) - 0.1745, this.radius / 2.0);
        this.ptSpanLabel = VectorUtil.translatePoint2D(this.center, Math.toRadians(this.arc.start + this.arc.extent / 2.0), this.radius + 10.0);
        final Point2D.Double translatePoint2D = VectorUtil.translatePoint2D(this.center, angle, this.radius);
        final Point2D.Double translatePoint2D2 = VectorUtil.translatePoint2D(this.center, angle2, this.radius);
        final Point2D.Double translatePoint2D3 = VectorUtil.translatePoint2D(translatePoint2D, angle, this.radius * 1.25);
        final Point2D.Double translatePoint2D4 = VectorUtil.translatePoint2D(translatePoint2D2, angle2, this.radius * 1.25);
        this.leg1.setLine(translatePoint2D, translatePoint2D3);
        this.leg2.setLine(translatePoint2D2, translatePoint2D4);
        this.theta = Math.toRadians(this.arc.extent);
        this.span = this.theta * this.radius;
        this.repaint();
    }
    
    public static double sqr(final double n) {
        return n * n;
    }
    
    public static int toInt(final double n) {
        return (n < 0.0) ? ((int)(n - 0.5)) : ((int)(n + 0.5));
    }
    
    public Color getArcColor() {
        return this.arcColor;
    }
    
    public void setArcColor(final Color arcColor) {
        this.arcColor = arcColor;
    }
    
    public double getRadius() {
        return this.radius;
    }
    
    public double getSpan() {
        return this.span;
    }
    
    public double getTheta() {
        return this.theta;
    }
    
    public class Popup extends JPopupMenu implements ActionListener
    {
        JMenuItem reset;
        JMenuItem help;
        
        public Popup() {
            this.reset = new JMenuItem();
            MapButtonPropertySetter.setHelp((AbstractButton)(this.help = new JMenuItem()));
            MapButtonPropertySetter.setReset((AbstractButton)this.reset);
            this.add(this.reset);
            this.add(this.help);
            this.reset.addActionListener(this);
            this.help.addActionListener(this);
        }
        
        public void actionPerformed(final ActionEvent actionEvent) {
            final Object source = actionEvent.getSource();
            if (source.equals(this.reset)) {
                AnglePanel.this.reset();
            }
            if (source.equals(this.help)) {
                System.out.println("FIX ME!!");
            }
        }
    }
    
    public class MyMouseAdapter extends MouseInputAdapter
    {
        Point currentPoint;
        Point oldPoint;
        Point translatePoint;
        JPanel parent;
        
        public MyMouseAdapter(final JPanel parent) {
            this.translatePoint = new Point();
            this.parent = parent;
        }
        
        public void mouseMoved(final MouseEvent mouseEvent) {
            this.oldPoint = mouseEvent.getPoint();
            AnglePanel.this.onL1 = false;
            AnglePanel.this.onL2 = false;
            AnglePanel.this.onRadius = false;
            int type;
            if (AnglePanel.this.elL1.contains(this.oldPoint.x, this.oldPoint.y)) {
                AnglePanel.this.onL1 = true;
                type = 13;
            }
            else if (AnglePanel.this.elL2.contains(this.oldPoint.x, this.oldPoint.y)) {
                AnglePanel.this.onL2 = true;
                type = 13;
            }
            else if (AnglePanel.this.elRadius.contains(this.oldPoint.x, this.oldPoint.y)) {
                AnglePanel.this.onRadius = true;
                type = 12;
            }
            else {
                type = 0;
            }
            AnglePanel.this.setCursor(Cursor.getPredefinedCursor(type));
        }
        
        public void mouseDragged(final MouseEvent mouseEvent) {
            this.currentPoint = mouseEvent.getPoint();
            this.translatePoint.x = this.currentPoint.x - this.oldPoint.x;
            this.translatePoint.y = this.currentPoint.y - this.oldPoint.y;
            this.oldPoint = this.currentPoint;
            if (AnglePanel.this.onL1) {
                final Point2D.Double access$600 = AnglePanel.this.ptL1;
                access$600.x += this.translatePoint.x;
                final Point2D.Double access$601 = AnglePanel.this.ptL1;
                access$601.y += this.translatePoint.y;
                AnglePanel.this.ptRadius = VectorUtil.translatePoint2D(AnglePanel.this.center, VectorUtil.findAngle(AnglePanel.this.center, AnglePanel.this.ptL1), AnglePanel.this.center.distance(AnglePanel.this.ptRadius.x, AnglePanel.this.ptRadius.y));
                AnglePanel.this.calculate();
            }
            if (AnglePanel.this.onL2) {
                final Point2D.Double access$602 = AnglePanel.this.ptL2;
                access$602.x += this.translatePoint.x;
                final Point2D.Double access$603 = AnglePanel.this.ptL2;
                access$603.y += this.translatePoint.y;
                AnglePanel.this.calculate();
            }
            if (AnglePanel.this.onRadius) {
                AnglePanel.this.ptRadius.x = this.currentPoint.x;
                AnglePanel.this.ptRadius.y = this.currentPoint.y;
                AnglePanel.this.ptRadius = VectorUtil.translatePoint2D(AnglePanel.this.center, VectorUtil.findAngle(AnglePanel.this.center, AnglePanel.this.ptL1), AnglePanel.this.center.distance(AnglePanel.this.ptRadius.x, AnglePanel.this.ptRadius.y));
                AnglePanel.this.calculate();
            }
        }
        
        public void mouseReleased(final MouseEvent mouseEvent) {
            this.mouseMoved(mouseEvent);
            if (mouseEvent.isPopupTrigger()) {
                AnglePanel.this.myPopup.show(this.parent, mouseEvent.getX(), mouseEvent.getY());
            }
        }
    }
    
    public class MyComponentAdapter extends ComponentAdapter
    {
        public void componentResized(final ComponentEvent componentEvent) {
            AnglePanel.this.reset();
        }
    }
}