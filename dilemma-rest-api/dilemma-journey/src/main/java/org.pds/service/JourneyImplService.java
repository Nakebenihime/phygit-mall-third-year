package org.pds.service;

import org.pds.model.*;
import org.pds.repository.CustomerRepository;
import org.pds.repository.JourneyRepository;
import org.pds.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class JourneyImplService implements ImplService<Journey> {

    private JourneyRepository journeyRepository;
    private CustomerRepository customerRepository;
    private StoreRepository storeRepository;
    private List<Vertex> nodes;
    private List<Edge> edges;

    @Autowired
    public void setJourneyRepository(JourneyRepository journeyRepository) {
        this.journeyRepository = journeyRepository;
    }

    @Autowired
    public void setCustomerRepository(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Autowired
    public void setStoreRepository(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    // retrieve stores which type corresponds to a given customer profile
    public List<Store> getJourney(String customerId) {
        List<Store> stores = new ArrayList<>();
        Customer customer = customerRepository.findById(customerId).get();
        String category = customer.getProfile();
        List<Store> allStores = storeRepository.findAll();
        System.out.println(allStores.toString());
        for (int i = 0; i < allStores.size(); i++) {
            System.out.println(i);
            if (allStores.get(i).getType() == category) {
                stores.add(stores.get(i));
            }
        }
        return stores;
    }

    public List<Store> getJourneyForAProfile(String profile) {
        List<Store> stores = new ArrayList<>();
        List<Store> allStores = storeRepository.findAll();
        for (int i = 0; i < allStores.size(); i++) {
            if (allStores.get(i).getType() == profile) {
                stores.add(allStores.get(i));
            }
        }
        // ajouter les noeuds du graphe correspondant aux magasins dans la collection journey
        return stores;
    }

    public void insertData() {
        // Customers
        customerRepository.insert(new Customer("Angélique", "KV", "OUTDOOR_ACTIVITIES"));
        customerRepository.insert(new Customer("Mélodie", "KV", "FASHION"));
        customerRepository.insert(new Customer("Jess", "Rm", "FOOD"));
        customerRepository.insert(new Customer("Yaya", "Np", "type"));
        // Stores
        storeRepository.insert(new Store("Nature & Découverte", "OUTDOOR_ACTIVITIES"));
        storeRepository.insert(new Store("Zara", "FASHION"));
        storeRepository.insert(new Store("H&M", "FASHION"));
        storeRepository.insert(new Store("Pull&Bear", "FASHION"));
        storeRepository.insert(new Store("Vapiano", "FOOD"));
        storeRepository.insert(new Store("Columbus Café", "FOOD"));
        storeRepository.insert(new Store("Magasin test", "type"));
        storeRepository.insert(new Store("Carrefour", "grande distribution"));
        // Journeys
        this.getJourneyForAProfile("OUTDOOR_ACTIVITIES");
        this.getJourneyForAProfile("FASHION");
        this.getJourneyForAProfile("FOOD");
        this.getJourneyForAProfile("type");
    }

    public Journey initAJourney() {
        String journeyId = "123";
        nodes = new ArrayList<Vertex>();
        edges = new ArrayList<Edge>();
        // All alley ids from commom Map modelisation
        //List<IntSeger> alleysFromAndroidModel = List.of(15, 56, 55, 51, 8, 35, 37, 34, 31, 26, 54);
        // Node_0 to Node_9 with alley ids
        Vertex location = new Vertex("Node_" + 1, "Alley_" + 15, 26.0f, 14.9f);
        Vertex location1 = new Vertex("Node_" + 2, "Alley_" + 56, 24.7f, 8.3f);
        Vertex location2 = new Vertex("Node_" + 3, "Alley_" + 55, 20.4f, 8.3f);
        Vertex location3 = new Vertex("Node_" + 4, "Alley_" + 51, 15.8f, 8.3f);
        Vertex location4 = new Vertex("Node_" + 5, "Alley_" + 8, 12.9f, 0.8f);
        Vertex location5 = new Vertex("Node_" + 6, "Alley_" + 35, 11.6f, 8.9f);
        Vertex location6 = new Vertex("Node_" + 7, "Alley_" + 37, 15.95f, 9.6f);
        Vertex location7 = new Vertex("Node_" + 8, "Alley_" + 34, 12.8f, 14.2f);
        Vertex location8 = new Vertex("Node_" + 9, "Alley_" + 31, 12.8f, 18.2f);
        Vertex location9 = new Vertex("Node_" + 10, "Alley_" + 26, 12.8f, 23.2f);
        Vertex location10 = new Vertex("Node_" + 1, "Alley_" + 54, 12.8f, 27.5f);
        nodes.add(location);
        nodes.add(location1);
        nodes.add(location2);
        nodes.add(location3);
        nodes.add(location4);
        nodes.add(location5);
        nodes.add(location6);
        nodes.add(location7);
        nodes.add(location8);
        nodes.add(location9);
        nodes.add(location10);
        // add edges between them
        addLane("Edge_0", 0, 1, 85);
        addLane("Edge_1", 1, 2, 85);
        addLane("Edge_2", 2, 3, 85);
        addLane("Edge_3", 3, 4, 85);
        addLane("Edge_4", 4, 5, 85);
        addLane("Edge_5", 5, 6, 85);
        addLane("Edge_6", 5, 7, 85);
        addLane("Edge_7", 7, 8, 85);
        addLane("Edge_8", 8, 9, 85);
        addLane("Edge_8", 9, 10, 85);
        // Lets check from location Loc_1 to Loc_10
        Graph graph = new Graph(nodes, edges);
        DijkstraAlgorithmService dijkstra = new DijkstraAlgorithmService(graph);
        dijkstra.exe(nodes.get(0));
        Journey journey = new Journey(journeyId, dijkstra.getPath(nodes.get(2)));
        System.out.println(journey.getNodes().get(1).getX());
        System.out.println(journey.getNodes().get(1).getY());
        journeyRepository.save(journey);
        return journey;
    }

    private void addLane(String laneId, int sourceLocNo, int destLocNo, int duration) {
        Edge lane = new Edge(laneId, nodes.get(sourceLocNo), nodes.get(destLocNo), duration);
        edges.add(lane);
    }

    @Override
    public Journey save(Journey journey) {
        return journeyRepository.save(journey);
    }

    @Override
    public List<Journey> findAll() {
        return journeyRepository.findAll();
    }

    @Override
    public Optional<Journey> findById(String id) {
        return journeyRepository.findById(id);
    }

    @Override
    public void deleteAll() {
        journeyRepository.deleteAll();
    }

    @Override
    public void deleteById(String id) {
        journeyRepository.deleteById(id);
    }
}
