<?php

App::uses('AppController', 'Controller');

/**
 * Drivers Controller
 *
 * @property Driver $Driver
 * @property PaginatorComponent $Paginator
 * @property SessionComponent $Session
 */
class DriversController extends AppController {

    /**
     * Components
     *
     * @var array
     */
    public $components = array('Paginator', 'Session');

    /**
     * index method
     *
     * @return void
     */
    public function api_index() {
        $this->Driver->recursive = 0;
        $x = $this->Paginator->paginate();
        //$this->set('drivers', );
        $this->set(array(
            'data' => $x,
            '_serialize' => array('data')
        ));
    }

    /**
     * view method
     *
     * @throws NotFoundException
     * @param string $id
     * @return void
     */
    public function view($id = null) {
        if (!$this->Driver->exists($id)) {
            throw new NotFoundException(__('Invalid driver'));
        }
        $options = array('conditions' => array('Driver.' . $this->Driver->primaryKey => $id));
        $this->set('driver', $this->Driver->find('first', $options));
    }

    /**
     * add method
     *
     * @return void
     */
    public function api_addnget() {
        $x = array();
        if ($this->request->is('post')) {
            $Driver = $this->Driver->find('first', array(
                'conditions' => array(
                    'Driver.deviceId' => $this->request->data['Driver']['deviceId'],
                    'Driver.email' => $this->request->data['Driver']['email'],
                )
                    )
            );
            if (empty($Driver)) {
                $this->request->data['Driver']['timestamp'] = time();
                $this->Driver->create();
                if ($this->Driver->save($this->request->data)) {
                    $x['error'] = 0;
                    $x['msg'] = "The driver has been saved.";
                    $x['Driver'] = $this->Driver->find('first', array('conditions' => array('Driver.id' => $this->Driver->getLastInsertID())));
                    $x['Driver'] = $x['Driver']['Driver'];
                } else {
                    $x['error'] = 1;
                    $x['msg'] = "The driver could not be saved. Please, try again.";
                    $x['Error'] = $this->Driver->validationErrors;
                }
            } else {
                $x['error'] = 0;
                $x['msg'] = "Existing Driver";
                $x['Driver'] = $Driver['Driver'];
            }
        } else {
            $Driver = $this->Driver->find('first', array(
                'conditions' => array()
                    )
            );
            $x['error'] = 0;
            $x['msg'] = "Existing Driver";
            $x['Driver'] = $Driver['Driver'];
        }
        $this->set(array(
            'data' => $x,
            '_serialize' => array('data')
        ));
    }

    /**
     * edit method
     *
     * @throws NotFoundException
     * @param string $id
     * @return void
     */
    public function edit($id = null) {
        if (!$this->Driver->exists($id)) {
            throw new NotFoundException(__('Invalid driver'));
        }
        if ($this->request->is(array('post', 'put'))) {
            if ($this->Driver->save($this->request->data)) {
                $this->Session->setFlash(__('The driver has been saved.'));
                return $this->redirect(array('action' => 'index'));
            } else {
                $this->Session->setFlash(__('The driver could not be saved. Please, try again.'));
            }
        } else {
            $options = array('conditions' => array('Driver.' . $this->Driver->primaryKey => $id));
            $this->request->data = $this->Driver->find('first', $options);
        }
    }

    /**
     * delete method
     *
     * @throws NotFoundException
     * @param string $id
     * @return void
     */
    public function delete($id = null) {
        $this->Driver->id = $id;
        if (!$this->Driver->exists()) {
            throw new NotFoundException(__('Invalid driver'));
        }
        $this->request->allowMethod('post', 'delete');
        if ($this->Driver->delete()) {
            $this->Session->setFlash(__('The driver has been deleted.'));
        } else {
            $this->Session->setFlash(__('The driver could not be deleted. Please, try again.'));
        }
        return $this->redirect(array('action' => 'index'));
    }

    /**
     * admin_index method
     *
     * @return void
     */
    public function admin_index() {
        $this->Driver->recursive = 0;
        $this->set('drivers', $this->Paginator->paginate());
    }

    /**
     * admin_view method
     *
     * @throws NotFoundException
     * @param string $id
     * @return void
     */
    public function admin_view($id = null) {
        if (!$this->Driver->exists($id)) {
            throw new NotFoundException(__('Invalid driver'));
        }
        $options = array('conditions' => array('Driver.' . $this->Driver->primaryKey => $id));
        $this->set('driver', $this->Driver->find('first', $options));
    }

    /**
     * admin_add method
     *
     * @return void
     */
    public function admin_add() {
        if ($this->request->is('post')) {
            $this->Driver->create();
            if ($this->Driver->save($this->request->data)) {
                $this->Session->setFlash(__('The driver has been saved.'));
                return $this->redirect(array('action' => 'index'));
            } else {
                $this->Session->setFlash(__('The driver could not be saved. Please, try again.'));
            }
        }
    }

    /**
     * admin_edit method
     *
     * @throws NotFoundException
     * @param string $id
     * @return void
     */
    public function admin_edit($id = null) {
        if (!$this->Driver->exists($id)) {
            throw new NotFoundException(__('Invalid driver'));
        }
        if ($this->request->is(array('post', 'put'))) {
            if ($this->Driver->save($this->request->data)) {
                $this->Session->setFlash(__('The driver has been saved.'));
                return $this->redirect(array('action' => 'index'));
            } else {
                $this->Session->setFlash(__('The driver could not be saved. Please, try again.'));
            }
        } else {
            $options = array('conditions' => array('Driver.' . $this->Driver->primaryKey => $id));
            $this->request->data = $this->Driver->find('first', $options);
        }
    }

    /**
     * admin_delete method
     *
     * @throws NotFoundException
     * @param string $id
     * @return void
     */
    public function admin_delete($id = null) {
        $this->Driver->id = $id;
        if (!$this->Driver->exists()) {
            throw new NotFoundException(__('Invalid driver'));
        }
        $this->request->allowMethod('post', 'delete');
        if ($this->Driver->delete()) {
            $this->Session->setFlash(__('The driver has been deleted.'));
        } else {
            $this->Session->setFlash(__('The driver could not be deleted. Please, try again.'));
        }
        return $this->redirect(array('action' => 'index'));
    }

}
