class Solution {
    static class Node {
        String name;
        Map<String, Node> childern = new HashMap<>();

        private String hashCode = null;

        public Node(String _name) {
            name = _name;
        }

        public void add(List<String> path) {
            Node cur = this;
            for (String file : path) {
                if (!cur.childern.containsKey(file)) {
                    cur.childern.put(file, new Node(file));
                }
                cur = cur.childern.get(file);
            }
        }

        public String getHashCode() {
            if(hashCode == null) {
                hashCode = computeHash();
            }
            return hashCode;
        }

        private String computeHash() {
            StringBuilder sb = new StringBuilder();
            List<Node> chars = new ArrayList<>();
            for(Node n: childern.values()) {
                chars.add(n);
            }

            if(chars.size() == 0)
                return null;

            chars.sort((a, b) -> a.name.compareTo(b.name));

            for (Node n: chars){
                sb.append('(');
                sb.append(n.name + n.getHashCode());
                sb.append(')');
            }

            return sb.toString();
        }

        public void print(String prefix) {
            System.out.println(prefix + name);
            System.out.println(getHashCode());
            for (Node child : childern.values()) {
                if (child == null) continue;
                child.print(prefix + name + "/");
            }
        }

    }

    private static void getGoodFiles(Node node, Map<String, Integer> occurs, List<String> cur, List<List<String>> ans) {
        if(occurs.containsKey(node.getHashCode()) && occurs.get(node.getHashCode()) > 1) return;

        cur.add(node.name);
        ans.add(new ArrayList<>(cur));

        for(Node n: node.childern.values())
            getGoodFiles(n, occurs, cur, ans);

        cur.remove(cur.size()-1);
    }

    private static void findOccurs(Node node, Map<String, Integer> occurs) {
        String key = node.getHashCode();
        if(key != null) {
            occurs.put(key, occurs.getOrDefault(node.getHashCode(), 0)+1);
        }

        for(Node n: node.childern.values()) {
            findOccurs(n, occurs);
        }
    }


    static Node root;

    public static List<List<String>> deleteDuplicateFolder(List<List<String>> paths) {
        root = new Node("");
        for (List<String> path : paths)
            root.add(path);

        Map<String, Integer> occurs = new HashMap<>();
        findOccurs(root, occurs);

        List<List<String>> ans = new ArrayList<>();
        for(Node n: root.childern.values())
            getGoodFiles(n, occurs, new ArrayList<>(), ans);

        return ans;
    }
}
