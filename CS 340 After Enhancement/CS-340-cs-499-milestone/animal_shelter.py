from pymongo import MongoClient, errors
from bson.objectid import ObjectId

class AnimalShelter(object):
    """ CRUD operations for Animal collection in MongoDB """

    def __init__(self, user, password):
        # Initializing the MongoClient. This helps to 
        # access the MongoDB databases and collections.
        # This is hard-wired to use the aac database, the 
        # animals collection, and the aac user.
        # Definitions of the connection string variables are
        # unique to the individual Apporto environment.
        #
        # You must edit the connection variables below to reflect
        # your own instance of MongoDB!
        #
        #
        HOST = 'localhost'
        PORT = 27017
        DB = 'aac'
        COL = 'animals'
        #
        # Initialize Connection
        #
        print('mongodb://%s:%s@%s:%d' % (user,password,HOST,PORT))
        self.client = MongoClient('mongodb://%s:%s@%s:%d' % (user,password,HOST,PORT))
        self.database = self.client['%s' % (DB)]
        self.collection = self.database['%s' % (COL)]

# Complete this create method to implement the C in CRUD.
    def create(self, data=None):
        if data is not None:
            # Tries to import data on error tell the user and give the error
            try: 
                result = self.database.animals.insert_one(data)  # data should be dictionary  
                return result.inserted_id is not None # If data has an id return true else false
            except errors.PyMongoError as e:
                print(f"An error during the insert process has occured: {e}")  
                return False # On error return false
            except TypeError as e:
                print(f"An error during the insert process has occured: {e}")  
                return False # On error return false
        else:
            raise Exception("Nothing to save, because data parameter is empty")


    
# Create method to implement the U in CRUD.
    def update(self, query=None, updateData=None):
        if (query and updateData) is not None: # Ensure that data is contained in both query and updateData
            # Try to update the database
            try:
                result = self.database.animals.update_many(query, {'$set': updateData})
                return result.modified_count # Return the number of modified entries
            except errors.PyMongoError as e:
                print(f"An error during the update process has occured: {e}")   
                return 0 # return 0 on error
            except TypeError as e:
                print(f"An error during the update process has occured: {e}")  
                return 0 # return 0 on error
        else:
            raise Exception("Nothing to update, because update data or query parameter is empty")
                
# Create method to implement the R in CRUD.
    def read(self, searchData=None):
        # Checks for data
        if searchData is not None:
            try:
                result = list(self.database.animals.find(searchData)) # List of data
                return result
            except errors.PyMongoError as e:
                print(f"An error during the query process has occured: {e}")  
                result = [] # On error return empty list an error   
                return result
            except TypeError as e:
                print(f"An error during the query process has occured: {e}")  
                result = [] # On error return empty list an error 
                return result
        else:
            raise Exception("Nothing to read, because search data parameter is empty")
        
# Create method to implement the D in CRUD.
    def delete(self, deleteData=None):
        if deleteData is not None: # Ensure that data is not none
            # Try to delete data
            try:    
                result = self.database.animals.delete_many(deleteData) # result of deleting data
                return result.deleted_count # return number deleted
            except errors.PyMongoError as e:
                print(f"An error during the delete process has occured: {e}")   
                return 0 # return 0 on error
            except TypeError as e:
                print(f"An error during the delete process has occured: {e}")  
                return 0 # return 0 on error
        else:    
            raise Exception("Nothing to delete, because delete data parameter is empty")
