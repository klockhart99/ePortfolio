# Install Database

### Note that the MongoDB database used in this notebook does work for both Linux and Windows

## Install Jupyter

1. Install Python 3.11 or later
   - https://www.python.org/
3. Using pip install Jupyter
   - In PowerShell run `pip install jupyterlab`
4. Verify that jupyterlab is working
   - In PowerShell run `jupyter lab`

## Install MongoDB
1. Install MongoDB
   - https://www.mongodb.com/
   - You can use either the local version or cloud version
2. Install MongoDB Compass
   - https://www.mongodb.com/products/tools/compass
3. Connect MongoDB Compass to MongoDB
   - For local MongoDB, the URL is by default `localhost:27017`
   - For cloud MongoDB use your information from Atlas
4. Verify that MongoDB is working
   - Create Table `aac`
   - Create Collection `animals` under `aac`
     
## Install Dependencies
1. Installing Dash
   - In PowerShell run `pip install Dash`
2. Installing Plotly[Express]
   - In PowerShell run `pip install Plotly[Express]`
3. Install numpy 
   - In PowerShell run `pip install numpy`
4. Install pandas
   - In PowerShell run `pip install pandas`
5. Install matplotlib
   - In PowerShell run `pip install matplotlib`
6. Install pymongo
   - In PowerShell run `pip install pymongo`

## Importing to MongoDB
1. Connect to MongoDB with MongoDB Compass
   - For local MongoDB, the URL is by default `localhost:27017`
   - For cloud MongoDB use your information from Atlas
2. Goto table `aac`
3. Goto collection `animals`
4. Import CSV
    - Import provide csv file `aac_shelter_outcomes.csv`
    - Click Import
5. Verify that data is now inside the collection 

# Running Jupyter

## Creating Users
In order to create a user, we will need to access to MongoDB shell. Do note we will create two users for security reasons, this is especially important for the cloud version of the database. Local database does not require a second user but once again it is recommended for the best security.
1. Connect to MongoDB with MongoDB Compass
   - For local MongoDB, the URL is by default `localhost:27017`
   - For cloud MongoDB use your information from Atlas
2. Open the MongoDB shell
3. Switch to admin database
   - In the shell `use admin`
4. Create an admin user
    - `db.createUser({ user: "<username>", pwd: "<password>", roles: [{ role: "userAdminAnyDatabase", db: "admin" }] })`
    - Replace `<username>` with username
    - Replace `<password>` with password
### This part is optional however it is suggested that you make a read-only user for the aac database to prevent unauthorized access to the admin account
5. Create a read-only user
    - `db.createUser({ user: "<username>", pwd: "<password>", roles: [{ role: "read", db: "aac" }] })`
    - Replace `<username>` with username
    - Replace `<password>` with password

## Modifying Notebook and Animal Shelter
1. Modifying animal_shelter.py
   - On line 19 modify `HOST = <Host>` where `<Host>` is the host.
     - For the database this is the URL of the database without the port.
   - On line 20 modify `Port = <Port>` where `<Port>` is the port.
2. Verify that `DB = aac` and `COL = animals`
   - DB is the table you made.
   - COL is the collection you made.
### For security and to protect the admin account it is highly recommended to use a read-only account during this step
3. Modify ProjectTwoDashboard.ipynb
   - Find `username = "aacuser"`, replace `aacuser` with the read-only account username
   - Find `password = "SNHULockhart"`, replace `SNHULockhart` with the read-only account password

## Running the Application
Simply run the cell and navigate to `http://localhost:8050` to view the webpage.
