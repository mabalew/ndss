import client
from enum import Enum


class Operation(Enum):
    ADD = 'add'
    UPDATE = 'update'
    READ = 'read'
    LIST = 'list'
    DELETE = 'delete'


class Format(Enum):
    TEXT = 'text'
    HTML = 'html'
    XML = 'xml'
    JSON = 'json'


class NDSClient:
    params = {}
    def __init__(self, operation, sgid, format, name=None, value=None):
        self.operation = operation
        self.sgid = sgid
        self.format = format
        self.name = name
        self.value = value

    def prepare_url(self, params):
        for key in params:
            print(key, params[key])

    def call(self):
        self.params['operation'] = self.operation.value
        self.params['sgid'] = self.sgid
        self.params['format'] = self.format.value
        self.params['name'] = self.name
        self.params['value'] = self.value
        self.prepare_url(self.params)

def main():
    print(client.SERVLET_URL)
    NDSClient(Operation.LIST, 'sensor', Format.JSON)

if __name__ == "__main__":
    main()
