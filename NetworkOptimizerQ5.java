import java.awt.*;
import java.util.*;
import java.util.List;
import javax.swing.*;

class Node {
    String name;
    int x, y; // Position for GUI

    Node(String name, int x, int y) {
        this.name = name;
        this.x = x;
        this.y = y;
    }
}

class Edge {
    String from, to;
    int cost, bandwidth;

    Edge(String from, String to, int cost, int bandwidth) {
        this.from = from;
        this.to = to;
        this.cost = cost;
        this.bandwidth = bandwidth;
    }
}

public class NetworkOptimizerQ5 extends JFrame {
    private Map<String, Node> nodes = new HashMap<>();
    private Map<String, List<Edge>> graph = new HashMap<>();
    private JTextArea outputArea;
    
    public NetworkOptimizerQ5() {
        setTitle("Network Optimizer");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel graphPanel = new GraphPanel();
        add(graphPanel, BorderLayout.CENTER);

        JPanel controls = new JPanel();
        controls.setLayout(new FlowLayout());

        JTextField nodeField = new JTextField(5);
        JTextField xField = new JTextField(3);
        JTextField yField = new JTextField(3);
        JButton addNodeBtn = new JButton("Add Node");

        JTextField fromField = new JTextField(5);
        JTextField toField = new JTextField(5);
        JTextField costField = new JTextField(3);
        JTextField bandwidthField = new JTextField(3);
        JButton addEdgeBtn = new JButton("Add Edge");

        JButton mstBtn = new JButton("Find MST");
        JButton dijkstraBtn = new JButton("Find Shortest Path");

        outputArea = new JTextArea(5, 50);
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);

        addNodeBtn.addActionListener(e -> {
            String name = nodeField.getText();
            int x = Integer.parseInt(xField.getText());
            int y = Integer.parseInt(yField.getText());
            addNode(name, x, y);
            graphPanel.repaint();
        });

        addEdgeBtn.addActionListener(e -> {
            String from = fromField.getText();
            String to = toField.getText();
            int cost = Integer.parseInt(costField.getText());
            int bandwidth = Integer.parseInt(bandwidthField.getText());
            addEdge(from, to, cost, bandwidth);
            graphPanel.repaint();
        });

        mstBtn.addActionListener(e -> findMST());
        dijkstraBtn.addActionListener(e -> {
            String start = JOptionPane.showInputDialog("Enter Start Node:");
            String end = JOptionPane.showInputDialog("Enter End Node:");
            findShortestPath(start, end);
        });

        controls.add(new JLabel("Node:"));
        controls.add(nodeField);
        controls.add(new JLabel("X:"));
        controls.add(xField);
        controls.add(new JLabel("Y:"));
        controls.add(yField);
        controls.add(addNodeBtn);
        
        controls.add(new JLabel("From:"));
        controls.add(fromField);
        controls.add(new JLabel("To:"));
        controls.add(toField);
        controls.add(new JLabel("Cost:"));
        controls.add(costField);
        controls.add(new JLabel("Bandwidth:"));
        controls.add(bandwidthField);
        controls.add(addEdgeBtn);

        controls.add(mstBtn);
        controls.add(dijkstraBtn);

        add(controls, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.SOUTH);
    }

    private void addNode(String name, int x, int y) {
        nodes.put(name, new Node(name, x, y));
        graph.putIfAbsent(name, new ArrayList<>());
    }

    private void addEdge(String from, String to, int cost, int bandwidth) {
        graph.get(from).add(new Edge(from, to, cost, bandwidth));
        graph.get(to).add(new Edge(to, from, cost, bandwidth));
    }

    private void findMST() {
        PriorityQueue<Edge> pq = new PriorityQueue<>(Comparator.comparingInt(e -> e.cost));
        Set<String> visited = new HashSet<>();
        List<Edge> mstEdges = new ArrayList<>();

        String startNode = nodes.keySet().iterator().next();
        visited.add(startNode);
        pq.addAll(graph.get(startNode));

        while (!pq.isEmpty() && visited.size() < nodes.size()) {
            Edge edge = pq.poll();
            if (!visited.contains(edge.to)) {
                visited.add(edge.to);
                mstEdges.add(edge);
                pq.addAll(graph.get(edge.to));
            }
        }

        outputArea.setText("Minimum Spanning Tree:\n");
        for (Edge edge : mstEdges) {
            outputArea.append(edge.from + " -> " + edge.to + " | Cost: " + edge.cost + "\n");
        }
    }

    private void findShortestPath(String start, String end) {
        PriorityQueue<Edge> pq = new PriorityQueue<>(Comparator.comparingInt(e -> e.bandwidth));
        Map<String, Integer> distances = new HashMap<>();
        Map<String, String> prev = new HashMap<>();

        for (String node : nodes.keySet()) distances.put(node, Integer.MAX_VALUE);
        distances.put(start, 0);
        pq.add(new Edge(start, start, 0, 0));

        while (!pq.isEmpty()) {
            Edge current = pq.poll();
            if (current.to.equals(end)) break;
            for (Edge neighbor : graph.get(current.to)) {
                int newDist = distances.get(current.to) + neighbor.bandwidth;
                if (newDist < distances.get(neighbor.to)) {
                    distances.put(neighbor.to, newDist);
                    prev.put(neighbor.to, current.to);
                    pq.add(new Edge(neighbor.to, neighbor.to, newDist, newDist));
                }
            }
        }

        List<String> path = new ArrayList<>();
        for (String at = end; at != null; at = prev.get(at)) path.add(at);
        Collections.reverse(path);

        outputArea.setText("Shortest Path from " + start + " to " + end + ":\n" + String.join(" -> ", path) + "\n");
    }

    class GraphPanel extends JPanel {
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            for (Node node : nodes.values()) {
                g.fillOval(node.x, node.y, 20, 20);
                g.drawString(node.name, node.x, node.y - 5);
            }
            for (List<Edge> edges : graph.values()) {
                for (Edge edge : edges) {
                    Node from = nodes.get(edge.from);
                    Node to = nodes.get(edge.to);
                    g.drawLine(from.x + 10, from.y + 10, to.x + 10, to.y + 10);
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new NetworkOptimizerQ5().setVisible(true));
    }
}