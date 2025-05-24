# SongsDB – Artist-Song Graph Database

## Overview
SongsDB is a Java-based music database system that uses an extensible hash table and 
an adjacency list graph to manage artist and song data. It processes a subset of the 
Million Song dataset and models the relationships between artists and the songs they record.

The project supports dynamic resizing of hash tables and tracks 
graph connectivity using Union-Find for component analysis.

## Features
- Extensible hash tables for artist and song names
  - Uses sfold string hash function with quadratic probing
  - Automatically resizes when load exceeds 50%
- Graph with adjacency list structure
  - Nodes represent artists and songs
  - Edges represent recording relationships
- Command-based interface
  - insert, remove, print (artist, song, graph)
- Computes connected components and reports graph stats

## How to Run
java GraphProject <initHashSize> <commandFile>
