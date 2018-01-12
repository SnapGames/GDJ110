# C'est toujours mieux avec une particule

Le jeu vidéo c'est comme la vie, c'est toujours avec une particule. Le moteur a déjà bien avancé, sont maintenant nécessaires les travaux d'affichage de niveaux sous forme de Tilemap et surtout, ce qui nous intéresse ici, un moteur de particules permettant de créer tout un tas d'effets visuels.

![File:FogParticlesHighSpeed.jpg - Wikimedia Commons COMMONS.WIKIMEDIA.ORG](https://storify.com/services/proxy/2/4bauj-25Dr0U3Ev2i1zpRA/https/upload.wikimedia.org/wikipedia/commons/c/c5/FogParticlesHighSpeed.jpg "File:FogParticlesHighSpeed.jpg - Wikimedia Commons")

## Après le nom, le moteur !

Oui, il est vrai qu'un nom à particule est toujours bien vu en société. On est plus smart avec une particule.
Il en va de même dans le monde des moteurs de jeux vidéo. La particule, ou 'particle' en anglais, car oui c'est plus smart en anglais, et un objet graphique élémentaire, permettant de créer des effets dits spéciaux, comme au cinéma. Une seule particule ne vous conduira nul part, mais un tas de particle (ou batch) permet de faire des choses très intéressantes.

Ces batch de particule ou "Particle System" (oui, toujours l'anglais, ou English pour les connoisseurs) est animé suivant des comportements définis. un comportement est un algorithme simulant un mouvement, une transformation en couleur ou en forme. il est également possible de cumuler ces comportements: 

- la neige, c'est de la pluie tombant moins vite, avec des variations de couleurs et de formes dans les particules simulant les flocons.

- le vent,c'est de la pluie sous forme de feuilles, sensible aux bourrasques automnales.

- les nappes de brouillard sont des particules plus grandes, semi-transparente, portée par le vent, un peu comme des flocons, mais TRES ralentis

Bref vous avez compris le principe.

Avec une centaine de ces particules, on doit être capable de simuler la pluie ou la neige, les feuilles mortes virevoletant dans les premiers frimas de l'automne, les flammes ou escarbilles d'un feu. On doit même être capable de simuler un essaim d'abeilles ou un nuage de ces foutus moustiques piquant et enquiquineurs. 

A nous, les torches animées et les lucioles luminescentes dans les couloirs sombres d'un nouveau donjon à explorer...
Mais je m'égare. Restons concentrés sur ce qui nous intéresse: La particule et ses systèmes.

## Particle System

Non ce n'est pas un nouveau groupe de boys d'origine africaine. Mais comme indiqué précédemment, c'est avec cette entité que nous allons animer un ensemble d'objets graphiques élémentaires, avec des comportements spéciaux. Nous pouvons donc identifier plusieurs objets:

- ParticleManager qui permettra de gerer l'ensemble du moteur de particules.

- ParticleSystem qui permettra de gérer un groupe de Particle's et de leur appliquer un ou plusieurs comportements (ici appeler ParticleBehavior)

- Particle qui est l'objet unitaire représentant un des animés par le système et ses comportements. il est possible de la décliner en RainDrop, Snowflake, Leaf, etc... en fonction de l'usage ciblé

- ParticleBehavior est le composant gérant les phases d'animation et de transformation. Lui aussi, sera décliné en plusieurs solution: RainBehavior, WindBehavior, SnowBehavior, FireBehavior, TorchBehavior, etc... je vais m'arrêter ici, je pense que vous avez perçu le concept :)

Nous voilà fin prêt à tenter l'implémentation d'un tel monstre consommateur de ressources graphiques et système. 

Qu'ils soient 2D ou 3D, les concepts restent identiques; le même découpage en objets et utilitaires demeure.


## Particle était son nom

La particule de base comporte quelques attributs indispensables: la position courante, la dernière position connue, pour pouvoir faire quelques effets sympa question animation, la vitesse, la durée de vie (très important dans le monde des particules, la durée de vie,comme nous le verrons plus tard), et d'autres attributs moins funky, mais utiles pour le rendu, la couleur, la transparence,etc... On peut aussi prévoir des attributs dynamiques si besoin, comme pour les objet graphique du moteur déjà en place.

![](http://buildnewgames.com/assets/article//particle-systems/setup.png "Setup a particle")

Il est important de noter que nos particules n'hériteront pas du fameux AbstractGameObject et n'implémenteront pas l'interface que nous avons préparé dans les [premiers chapitres des cours](https://classroom.google.com/c/NzI2ODQ3NjU2MFpa/t/NzI2NzkxODkyN1pa "lire le chapitre correspondant"), GameObject.

C'est au niveau du système de particule que cela se passera (ParticleSystem implémente GameObject, et hérite de AbstractGameObject, lui) afin de le gérer comme n'importe quel objet de jeu du moteur.

Comme indiqué en préambule, les Particle's ont une durée de vie. non pas pour les détruire, mais pour les réutiliser !
En effet, un tableau de particule est consommateur en ressources, aussi, si lm'on passe son temps a allouer de la mémoire et à la désallouer au gré de la durée de vie de celles-ci, on se retrouve avec un hachage en règle de l'espace mémoire. Aussi, pour éviter cela, on doit réutiliser les objets déjà instanciés

C'est pour cela qu'on va utiliser des Behavior pour animer le tout, ainsi on n'impacte pas les objets "particules" avec les différence de simulation, seuls les comportements rattaché au particules changeront. Malinx, le Lynx ! :)

Nous aurons donc un tableau de particules instanciées au niveau du moteur de particules, et celles vivantes (dont la durée de vie est supérieure à 0) seront référencées dans des Systèmes (ParticleSystem) pour être animées/affichées

## References

Pour plus d'information, ou juste pour satisfaire votre curiosité, voilà de quoi vous alimenter:

- http://buildnewgames.com/particle-systems/
- http://www.pygame.org/project-PyIgnition-1527-.html
- https://stackoverflow.com/questions/14824996/pygame-particle-effects
