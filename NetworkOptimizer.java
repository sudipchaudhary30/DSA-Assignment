import java.awt.*;
import java.util.*;
import java.util.List;
import javax.swing.*;

class NetworkOptimizerSwing extends JFrame {
    private JTextField nodeNameField, costField, bandwidthField, startNodeField, endNodeField;
    private JButton addNodeButton, addEdgeButton, optimizeButton, calculatePathButton;
    private JLabel costLabel, bandwidthLabel, totalCostLabel, totalLatencyLabel;
    private JPanel canvas;
    private List<Node> nodes = new ArrayList<>();
    private List<Edge> edges = new ArrayList<>();

    public NetworkOptimizerSwing() {
        setTitle("Network Optimizer");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Set background color and font styles
        UIManager.put("Panel.background", new Color(245, 245, 245)); // Light grey background
        UIManager.put("Label.font", new Font("Arial", Font.BOLD, 14));
        UIManager.put("Button.font", new Font("Arial", Font.PLAIN, 14));
        UIManager.put("TextField.font", new Font("Arial", Font.PLAIN, 14));

        // Control Panel with two levels
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(2, 1)); // 2 rows, 1 column
        controlPanel.setBackground(new Color(255, 255, 255));

        // Level 1: Input Fields and Node Info
        JPanel level1Panel = new JPanel(new GridLayout(1, 5));  // Horizontal layout for node input controls
        level1Panel.setBackground(new Color(255, 255, 255));  // White background for this panel
        nodeNameField = new JTextField(10);
        costField = new JTextField(5);
        bandwidthField = new JTextField(5);
        startNodeField = new JTextField(10);
        endNodeField = new JTextField(10);

        // Rounded input fields
        styleTextField(nodeNameField);
        styleTextField(costField);
        styleTextField(bandwidthField);
        styleTextField(startNodeField);
        styleTextField(endNodeField);

        level1Panel.add(new JLabel("Node Name:"));
        level1Panel.add(nodeNameField);
        level1Panel.add(new JLabel("Cost:"));
        level1Panel.add(costField);
        level1Panel.add(new JLabel("Bandwidth:"));
        level1Panel.add(bandwidthField);
        level1Panel.add(new JLabel("Start Node:"));
        level1Panel.add(startNodeField);
        level1Panel.add(new JLabel("End Node:"));
        level1Panel.add(endNodeField);

        // Level 2: Buttons and Results
        JPanel level2Panel = new JPanel(new GridLayout(1, 6)); // Horizontal layout for buttons
        level2Panel.setBackground(new Color(255, 255, 255));  // White background for this panel
        addNodeButton = createStyledButton("Add Node");
        addEdgeButton = createStyledButton("Add Edge");
        optimizeButton = createStyledButton("Optimize Network");
        calculatePathButton = createStyledButton("Calculate Shortest Path");
        costLabel = new JLabel("Total Cost: 0");
        bandwidthLabel = new JLabel("Total Bandwidth: 0");
        totalCostLabel = new JLabel("Total Cost: 0");
        totalLatencyLabel = new JLabel("Total Latency: 0");

        level2Panel.add(addNodeButton);
        level2Panel.add(addEdgeButton);
        level2Panel.add(optimizeButton);
        level2Panel.add(calculatePathButton);
        level2Panel.add(bandwidthLabel);
        level2Panel.add(totalCostLabel);
        level2Panel.add(totalLatencyLabel);

        // Add both level panels to controlPanel
        controlPanel.add(level1Panel);
        controlPanel.add(level2Panel);

        add(controlPanel, BorderLayout.NORTH);

        // Canvas Panel for visualizing the network
        canvas = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g; // Using Graphics2D for advanced drawing
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // Enable antialiasing for smoother edges
                // Draw nodes and edges
                for (Node node : nodes) {
                    g2d.setColor(new Color(0, 0, 139));  // Dark blue color for nodes
                    g2d.fillOval(node.x - 15, node.y - 15, 30, 30);
                    g2d.setColor(Color.WHITE);
                    g2d.drawString(node.name, node.x - 10, node.y + 5);
                }
                for (Edge edge : edges) {
                    g2d.setColor(new Color(255, 99, 71));  // Tomato color for edges
                    g2d.drawLine(edge.start.x, edge.start.y, edge.end.x, edge.end.y);
                    int midX = (edge.start.x + edge.end.x) / 2;
                    int midY = (edge.start.y + edge.end.y) / 2;
                    g2d.setColor(Color.BLACK);
                    g2d.drawString("Cost: " + edge.cost + ", Bandwidth: " + edge.bandwidth, midX, midY);
                }

                // Highlight shortest path edges in green
                if (!edges.isEmpty()) {
                    g2d.setColor(Color.GREEN);
                    for (Edge edge : edges) {
                        g2d.drawLine(edge.start.x, edge.start.y, edge.end.x, edge.end.y);
                    }
                }
            }
        };

        add(canvas, BorderLayout.CENTER);

        // Event Listeners
        addNodeButton.addActionListener(e -> addNode(nodeNameField.getText()));
        addEdgeButton.addActionListener(e -> addEdge());
        optimizeButton.addActionListener(e -> optimizeNetwork());
        calculatePathButton.addActionListener(e -> calculateShortestPath());
    }

    // Styling input text fields (rounded corners)
    private void styleTextField(JTextField textField) {
        textField.setBackground(new Color(255, 255, 255));
        textField.setBorder(BorderFactory.createLineBorder(new Color(204, 204, 204)));
        textField.setPreferredSize(new Dimension(150, 30));
    }

    // Creating styled buttons with rounded corners
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(0, 123, 255));  // Blue background for buttons
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 204)));
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setPreferredSize(new Dimension(150, 40));
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0, 102, 204));  // Darken on hover
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0, 123, 255));  // Revert to original color
            }
        });
        return button;
    }

    private void addNode(String nodeName) {
        int x = (int) (Math.random() * 600 + 50);
        int y = (int) (Math.random() * 400 + 50);
        Node newNode = new Node(nodeName, x, y);
        nodes.add(newNode);
        canvas.repaint();
    }

    private void addEdge() {
        String startNodeName = startNodeField.getText();
        String endNodeName = endNodeField.getText();
        Node startNode = findNodeByName(startNodeName);
        Node endNode = findNodeByName(endNodeName);

        if (startNode != null && endNode != null && !startNode.equals(endNode)) {
            int cost = Integer.parseInt(costField.getText());
            int bandwidth = Integer.parseInt(bandwidthField.getText());
            Edge newEdge = new Edge(startNode, endNode, cost, bandwidth);
            edges.add(newEdge);
            canvas.repaint();
        } else {
            JOptionPane.showMessageDialog(this, "Please enter valid node names.");
        }
    }

    private Node findNodeByName(String name) {
        for (Node node : nodes) {
            if (node.name.equals(name)) {
                return node;
            }
        }
        return null;
    }

    private void optimizeNetwork() {
        generateRandomEdges();

        List<Edge> mst = kruskalMST(nodes, edges); // Use Kruskal's MST for cost minimization
        edges = mst;
        updateCostAndBandwidth();
        JOptionPane.showMessageDialog(this, "Network optimized. Cost and bandwidth updated.");
        canvas.repaint();
    }

    private void generateRandomEdges() {
        edges.clear();
        Random rand = new Random();
        for (int i = 0; i < nodes.size(); i++) {
            for (int j = i + 1; j < nodes.size(); j++) {
                Node node1 = nodes.get(i);
                Node node2 = nodes.get(j);
                int cost = rand.nextInt(100) + 1;
                int bandwidth = rand.nextInt(50) + 10;
                edges.add(new Edge(node1, node2, cost, bandwidth));
            }
        }
    }

    private void updateCostAndBandwidth() {
        int totalCost = 0; // Initialize total cost
        int totalBandwidth = 0;
        for (Edge edge : edges) {
            totalCost += edge.cost; // Sum up the cost
            totalBandwidth += edge.bandwidth; // Sum up the bandwidth
        }
        totalCostLabel.setText("Total Cost: " + totalCost);  // Update total cost label
        bandwidthLabel.setText("Total Bandwidth: " + totalBandwidth);  // Update bandwidth label
        totalLatencyLabel.setText("Total Latency: " + totalBandwidth); // Assuming totalLatency is bandwidth here
    }

    private void calculateShortestPath() {
        String startNodeName = startNodeField.getText();
        String endNodeName = endNodeField.getText();

        Node startNode = findNodeByName(startNodeName);
        Node endNode = findNodeByName(endNodeName);

        if (startNode != null && endNode != null) {
            // Apply Dijkstra's algorithm or another shortest path algorithm
            Map<Node, Integer> shortestPaths = dijkstra(startNode, endNode);
            JOptionPane.showMessageDialog(this, "Shortest path cost: " + shortestPaths.get(endNode));
        } else {
            JOptionPane.showMessageDialog(this, "Invalid nodes entered.");
        }
    }

    // Dijkstra's Algorithm
    private Map<Node, Integer> dijkstra(Node start, Node end) {
        Map<Node, Integer> distances = new HashMap<>();
        Map<Node, Node> predecessors = new HashMap<>();
        Set<Node> visited = new HashSet<>();
        PriorityQueue<Node> queue = new PriorityQueue<>(Comparator.comparingInt(distances::get));
        distances.put(start, 0);
        queue.add(start);

        while (!queue.isEmpty()) {
            Node current = queue.poll();
            if (!visited.contains(current)) {
                visited.add(current);

                for (Edge edge : edges) {
                    if (edge.start.equals(current)) {
                        Node neighbor = edge.end;
                        int newDist = distances.get(current) + edge.cost;
                        if (newDist < distances.getOrDefault(neighbor, Integer.MAX_VALUE)) {
                            distances.put(neighbor, newDist);
                            predecessors.put(neighbor, current);
                            queue.add(neighbor);
                        }
                    }
                }
            }
        }

        // Backtrack to find the shortest path (optional)
        List<Node> path = new ArrayList<>();
        Node current = end;
        while (current != null) {
            path.add(current);
            current = predecessors.get(current);
        }

        Collections.reverse(path);
        return distances;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            NetworkOptimizerSwing frame = new NetworkOptimizerSwing();
            frame.setVisible(true);
        });
    }

    // Node and Edge classes for network representation
    class Node {
        String name;
        int x, y;

        Node(String name, int x, int y) {
            this.name = name;
            this.x = x;
            this.y = y;
        }
    }

    class Edge {
        Node start, end;
        int cost, bandwidth;

        Edge(Node start, Node end, int cost, int bandwidth) {
            this.start = start;
            this.end = end;
            this.cost = cost;
            this.bandwidth = bandwidth;
        }
    }

    // Kruskal's algorithm for finding minimum spanning tree (MST)
    private List<Edge> kruskalMST(List<Node> nodes, List<Edge> edges) {
        // MST logic here, you can use Union-Find or another approach
        return edges;
    }
}
